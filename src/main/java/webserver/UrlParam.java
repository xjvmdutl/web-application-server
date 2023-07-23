package webserver;

import java.util.HashMap;
import java.util.Map;
import util.HttpRequestUtils;

public class UrlParam {

  private String url;
  private Map<String, String> params = new HashMap<>();

  public UrlParam(String url) {
    this.url = url;
  }

  public String getUrl() {
    return url;
  }


  public Map<String, String> getParams() {
    return params;
  }

  public void addParam(String params) {
    this.params = HttpRequestUtils.parseQueryString(params);
  }
}
