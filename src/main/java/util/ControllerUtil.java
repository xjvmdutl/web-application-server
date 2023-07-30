package util;

import db.DataBase;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import model.User;

public class ControllerUtil {
  private String requestUrl;

  private String requestMethod;
  private Map<String, String> requestCookie = new HashMap<>();

  private Map<String, String> params = new HashMap<>();

  private Map<String, String> responseCookie = new HashMap<>();
  private String responseMethod;
  private String responseUrl;

  private byte[] body;
  public ControllerUtil() {
  }

  public String getRequestMethod() {
    return requestMethod;
  }

  public String getRequestUrl() {
    return requestUrl;
  }


  public Map<String, String> getParams() {
    return params;
  }

  public Map<String, String> getResponseCookie() {
    return responseCookie;
  }

  public void addParam(String params) {
    this.params.putAll(HttpRequestUtils.parseQueryString(params));
  }

  public void matchingUrl() throws IOException {
    if (requestUrl.equals("/user/create")) {
      this.responseMethod = "302";
      User user = getUser();
      addUser(user);
      responseUrl = "/index.html";
    } else if (requestUrl.equals("/user/login")) {
      this.responseMethod = "302";
      String id = params.get("userId");
      String password = params.get("password");
      boolean isLogined = isLogined(id, password);
      appendCookie("logined", Boolean.toString(isLogined));
      responseUrl = isLogined ? "/index.html" : "/user/login_failed.html";
    } else if (requestUrl.equals("/user/list")) {
      boolean logined = Boolean.parseBoolean(requestCookie.get("logined"));
      if(logined){
        this.responseMethod = "200";
        Collection<User> users = DataBase.findAll();
        String html = appendHTML(users);
        this.body = html.getBytes();
        return;
      }
      this.responseMethod = "302";
      responseUrl = "/user/login.html";
    }else {
      this.responseMethod = "200";
      this.responseUrl = requestUrl;
    }
    this.body = Files.readAllBytes(new File("./webapp" + this.responseUrl).toPath());
  }

  private static String appendHTML(Collection<User> users) {
    StringBuilder sb = new StringBuilder();
    sb.append("<table border='1'>");
    for (User user : users) {
      sb.append("<tr>");
      sb.append("<td>" + user.getUserId() + "</td>");
      sb.append("<td>" + user.getName() + "</td>");
      sb.append("<td>" + user.getEmail() + "</td>");
      sb.append("</tr>");
    }
    sb.append("</tr>");
    sb.append("</table>");
    return sb.toString();
  }

  private User getUser() {
    if (!getParams().isEmpty()) {
      return new User(
          params.get("userId"),
          params.get("password"),
          params.get("name"),
          params.get("email")
      );
    }
    throw new IllegalArgumentException("잘못된 User를 입력하셨습니다.");
  }

  private void appendCookie(String key, String value) {
    responseCookie.put(key, value);
  }

  public String getResponseMethod() {
    return responseMethod;
  }

  public void addUser(User user) {
    DataBase.addUser(user);
  }

  private boolean isLogined(String id, String password) {
    User findUser = DataBase.findUserById(id);
    return findUser != null && findUser.getPassword().equals(password);
  }

  public void addUrlAndMethod(String url, String method) {
    this.requestUrl = url;
    this.requestMethod = method;
  }

  public Map<String, String> getRequestCookie() {
    return requestCookie;
  }

  public void addRequestCookie(Map<String, String> requestCookie) {
    this.requestCookie = requestCookie;
  }

  public byte[] getBody() {
    return body;
  }

  public String getResponseUrl() {
    return responseUrl;
  }
}
