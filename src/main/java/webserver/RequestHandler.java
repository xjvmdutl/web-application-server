package webserver;

import db.DataBase;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.IOUtils;
import util.UrlParseUtils;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Collection;
import java.util.Map;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder response = new StringBuilder();
            String line = reader.readLine();
            if (line == null) {
                return;
            }
            String totalUrl = UrlParseUtils.parseUrl(line);
            String method = UrlParseUtils.parseMethod(line);
            String url = UrlParseUtils.separateUrl(totalUrl);
            Map<String, String> params = HttpRequestUtils.parseQueryString(UrlParseUtils.separateParam(totalUrl));
            HttpRequestUtils.parseQueryString(url);
            int contentLength = 0;
            String cookies = null;
            boolean isCss = false;
            while (!"".equals(line)) {
                line = reader.readLine();
                if (line.isEmpty())
                    break;
                String[] keyValue = line.split(":");
                String key = keyValue[0];
                String value = keyValue[1].trim();
                switch (key) {
                    case "Content-Length":
                        contentLength = Integer.parseInt(value);
                        break;
                    case "Accept":
                        isCss = value.contains("text/css");
                        break;
                    case "Cookie":
                        cookies = value;
                        break;
                    default:
                        break;
                }
                response.append(line).append("\n");
            }
            DataOutputStream dos = new DataOutputStream(out);
            byte[] body = null;
            if (method.equals("GET")) {
                switch (url) {
                    case "/user/create":
                        User user = new User(params.get("userId"), params.get("password"), params.get("name"), params.get("email"));
                        DataBase.addUser(user);
                        body = "".getBytes();
                        response302Header(dos, "/index.html", body.length);
                        break;
                    case "/user/form.html":
                        body = Files.readAllBytes(new File("./webapp" + url).toPath());
                        response200Header(dos, body.length, isCss);
                        break;
                    case "/user/list":
                        if (cookies == null || cookies.isEmpty()) {
                            body = "".getBytes();
                            response302Header(dos, "/user/form.html", body.length);
                            break;
                        }
                        Boolean isLogin = Boolean.parseBoolean(HttpRequestUtils.parseCookies(cookies).getOrDefault("logined", "false"));
                        if (isLogin) {
                            StringBuilder builder = new StringBuilder();
                            Collection<User> users = DataBase.findAll();
                            users.forEach(builder::append);
                            body = builder.toString().getBytes();
                            response200Header(dos, body.length, isCss);
                        } else {
                            body = "".getBytes();
                            response302Header(dos, "/user/form.html", body.length);
                        }
                        break;
                    case "/index.html":
                    case "/user/login.html":
                        body = Files.readAllBytes(new File("./webapp" + url).toPath());
                        response200Header(dos, body.length, isCss);
                        break;
                    default:
                        body = response.toString().getBytes();
                        response200Header(dos, body.length, isCss);
                        break;
                }
            }
            if (method.equals("POST")) {
                Map<String, String> postBody;
                switch (url) {
                    case "/user/create":
                        postBody = HttpRequestUtils.parseQueryString(IOUtils.readData(reader, contentLength));
                        User user = new User(postBody.get("userId"), postBody.get("password"), postBody.get("name"), postBody.get("email"));
                        body = "".getBytes();
                        response302Header(dos, "/index.html", body.length);
                        break;
                    case "/user/login":
                        postBody = HttpRequestUtils.parseQueryString(IOUtils.readData(reader, contentLength));
                        String userId = postBody.get("userId");
                        String password = postBody.get("password");
                        User findUser = DataBase.findUserById(userId);
                        body = "".getBytes();
                        if (findUser != null && findUser.getPassword().equals(password) && findUser.getUserId().equals(userId)) {
                            response302Header(dos, "/index.html", body.length, true);
                        } else {
                            response302Header(dos, "/index.html", body.length, false);
                        }
                        break;
                    default:
                        body = response.toString().getBytes();
                        response200Header(dos, body.length, isCss);
                        break;
                }
            }
            responseBody(dos, body);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent, boolean isCss) throws IOException {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            if (isCss){
                dos.writeBytes("Content-Type: text/css\r\n");
            }else {
                dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            }
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void response302Header(DataOutputStream dos, String location, int lengthOfBodyContent, Boolean isLogined) {
        try {
            dos.writeBytes("HTTP/1.1 302 Found \r\n");
            dos.writeBytes("Location: " + location + "\r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Set-Cookie: logined=" + isLogined + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }


    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
