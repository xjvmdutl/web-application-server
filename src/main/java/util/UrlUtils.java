package util;

import java.util.Optional;
import java.util.StringTokenizer;
import webserver.UrlParam;

public class UrlUtils {

  public static String getFirstLine(String line) {
    if (line.split(" ").length != 3) {
      throw new IllegalArgumentException("URL 형식이 올바르지 않습니다.");
    }
    StringTokenizer tokenizer = new StringTokenizer(line, " ");
    String method = tokenizer.nextToken();
    String url = tokenizer.nextToken();
    String httpVersion = tokenizer.nextToken();

    return url;
  }


  public static String getRequestUrl(String url) {
    int index = url.indexOf("?");
    return url.substring(0, index);
  }

  public static UrlParam getDivideContentFromUrl(String requestUrl) {
    int index = requestUrl.indexOf("?");
    if (index == -1) {
      return new UrlParam(requestUrl);
    } else {
      UrlParam urlParam = new UrlParam(requestUrl.substring(0, index));
      urlParam.addParam(requestUrl.substring(index + 1));
      return urlParam;
    }
  }
}
