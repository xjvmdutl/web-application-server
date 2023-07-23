package util;

import java.util.Optional;
import java.util.StringTokenizer;
import webserver.UrlParam;

public class UrlUtils {

  public static UrlParam getFirstLine(String line) {
    if (line.split(" ").length != 3) {
      throw new IllegalArgumentException("URL 형식이 올바르지 않습니다.");
    }
    StringTokenizer tokenizer = new StringTokenizer(line, " ");
    String method = tokenizer.nextToken();
    String url = tokenizer.nextToken();
    String httpVersion = tokenizer.nextToken();

    return new UrlParam(method, url);
  }


  public static String getRequestUrl(String url) {
    int index = url.indexOf("?");
    return url.substring(0, index);
  }

  public static UrlParam getDivideContentFromUrl(UrlParam requestUrlParam) {
    int index = requestUrlParam.getUrl().indexOf("?");
    if (index == -1) {
      return requestUrlParam;
    } else {
      String url = requestUrlParam.getUrl();
      UrlParam urlParam = new UrlParam(requestUrlParam.getMethod(), url.substring(0, index));
      urlParam.addParam(url.substring(index + 1));
      return urlParam;
    }
  }

  public static UrlParam addPostParam(UrlParam urlParam) {
    if(urlParam.getMethod().equals("POST")){

      return urlParam;
    }
    return urlParam;
  }

  public static int getContentLength(String line) {
    if(line.startsWith("Content-Length: ")){
      String contentLength = line.split(" ")[1];
      return Integer.parseInt(contentLength);
    }
    return 0;
  }


}
