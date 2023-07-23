package util;

import java.util.HashMap;
import java.util.Map;

public class ControllerUtil {

  private String requestMethod;
  private String requestUrl;

  private String responseMethod;
  private Map<String, String> params = new HashMap<>();

  public ControllerUtil(String requestMethod, String requestUrl) {
    this.requestMethod = requestMethod;
    this.requestUrl = requestUrl;
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

  public void addParam(String params) {
    this.params.putAll(HttpRequestUtils.parseQueryString(params));
  }

  public String getResponseUrl() {
    if (requestUrl.equals("/user/create")) {
      this.responseMethod = "302";
      return "/index.html";
    }
    this.responseMethod = "200";
    return requestUrl;
  }

  public String getResponseMethod() {
    return responseMethod;
  }
}
