package webserver;

import java.util.HashMap;
import java.util.Map;
import util.HttpRequestUtils;

public class UrlParam {

  private String method;
  private String url;
  private Map<String, String> params = new HashMap<>();

  public UrlParam(String method, String url) {
    this.method = method;
    this.url = url;
  }

  public String getMethod() {
    return method;
  }

  public String getUrl() {
    return url;
  }


  public Map<String, String> getParams() {
    return params;
  }

  public void addParam(String params) {
    this.params.putAll(HttpRequestUtils.parseQueryString(params));
  }

  public String getReturnUrl() {
    if (url.equals("/user/create")) {
      return "/index.html";
    } else {
      return url;
    }
  }
}
