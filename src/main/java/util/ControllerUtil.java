package util;

import db.DataBase;
import java.util.HashMap;
import java.util.Map;
import model.User;

public class ControllerUtil {


  private String requestMethod;
  private String requestUrl;

  private String responseMethod;
  private Map<String, String> params = new HashMap<>();

  private String cookie;


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

  public String getCookie() {
    return cookie;
  }

  public void addParam(String params) {
    this.params.putAll(HttpRequestUtils.parseQueryString(params));
  }

  public String getResponseUrl() {
    if (requestUrl.equals("/user/create")) {
      this.responseMethod = "302";
      User user = getUser();
      addUser(user);
      return "/index.html";
    } else if (requestUrl.equals("/user/login")) {
      this.responseMethod = "302";
      String id = params.get("userId");
      String password = params.get("password");
      boolean isLogined = isLogined(id, password);
      setCookie(isLogined);
      return isLogined ? "/index.html" : "/user/login_failed.html";
    }
    this.responseMethod = "200";
    return requestUrl;
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

  private void setCookie(boolean isLogined) {
    cookie = "isLogined=" + isLogined;
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
}
