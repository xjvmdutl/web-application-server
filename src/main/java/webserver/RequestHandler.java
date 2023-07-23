package webserver;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.IOUtils;
import util.UrlUtils;

public class RequestHandler extends Thread {

  private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);
  private static final int NOT_CONTENT_LENGTH = 0;

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
      String line;
      String url = "";
      boolean isFirstLine = true;
      boolean isPost = false;
      int contentLength = 0;
      UrlParam urlParam = null;
      while (!"".equals(line = reader.readLine())) {
        if (line == null) {
          return;
        }
        if (isFirstLine) {
          urlParam = UrlUtils.getDivideContentFromUrl(UrlUtils.getFirstLine(line));
          url = urlParam.getUrl();
          isFirstLine = false;
          if (urlParam.getMethod().equals("POST")) {
            isPost = true;
          }
        }
        int length = UrlUtils.getContentLength(line);
        if (length != NOT_CONTENT_LENGTH) {
          contentLength = length;
        }
      }
      DataOutputStream dos = new DataOutputStream(out);
      appendBodyData(reader, contentLength, urlParam);
      User user = getUser(urlParam);

      byte[] body = Files.readAllBytes(new File("./webapp" + url).toPath());
      response200Header(dos, body.length);
      responseBody(dos, body);
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

  private static void appendBodyData(BufferedReader reader, int contentLength, UrlParam urlParam)
      throws IOException {
    if(contentLength != NOT_CONTENT_LENGTH){
      String body = IOUtils.readData(reader, contentLength);
      urlParam.addParam(body);
    }
  }

  private User getUser(UrlParam urlParam) {
    if (urlParam == null && urlParam.getParams().isEmpty()) {
      return null;
    }
    Map<String, String> params = urlParam.getParams();
    return new User(
        params.get("userId"),
        params.get("password"),
        params.get("name"),
        params.get("email")
    );

  }

  private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
    try {
      dos.writeBytes("HTTP/1.1 200 OK \r\n");
      dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
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
