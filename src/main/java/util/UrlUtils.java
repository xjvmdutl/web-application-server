package util;

import java.util.StringTokenizer;

public class UrlUtils {

  public static ControllerUtil getFirstLine(String line) {
    if (line.split(" ").length != 3) {
      throw new IllegalArgumentException("URL 형식이 올바르지 않습니다.");
    }
    StringTokenizer tokenizer = new StringTokenizer(line, " ");
    String method = tokenizer.nextToken();
    String url = tokenizer.nextToken();
    String httpVersion = tokenizer.nextToken();

    return new ControllerUtil(method, url);
  }


  public static ControllerUtil getDivideContentFromUrl(ControllerUtil requestUrlParam) {
    int index = requestUrlParam.getRequestUrl().indexOf("?");
    if (index == -1) {
      return requestUrlParam;
    } else {
      String url = requestUrlParam.getRequestUrl();
      ControllerUtil urlParam = new ControllerUtil(requestUrlParam.getRequestMethod(), url.substring(0, index));
      urlParam.addParam(url.substring(index + 1));
      return urlParam;
    }
  }

  public static int getContentLength(String line) {
    if(line.startsWith("Content-Length: ")){
      String contentLength = line.split(" ")[1];
      return Integer.parseInt(contentLength);
    }
    return 0;
  }


}
