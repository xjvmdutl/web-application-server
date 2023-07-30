package util;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class UrlUtils {

  public static Map<String, String> getFirstLine(String line) {
    if (line.split(" ").length != 3) {
      throw new IllegalArgumentException("URL 형식이 올바르지 않습니다.");
    }
    StringTokenizer tokenizer = new StringTokenizer(line, " ");
    String method = tokenizer.nextToken();
    String url = tokenizer.nextToken();
    String httpVersion = tokenizer.nextToken();
    Map<String, String> result = new HashMap<>();
    result.put("url", url);
    result.put("method", method);
    return result;
  }


  public static void getDivideContentFromUrl(ControllerUtil controllerUtil, Map<String, String> requestUrlParam) {
    int index = requestUrlParam.get("url").indexOf("?");
    if (index == -1) {
      controllerUtil.addUrlAndMethod(requestUrlParam.get("url"), requestUrlParam.get("method"));
    } else {
      String url = requestUrlParam.get("url");
      controllerUtil.addUrlAndMethod(url.substring(0, index), requestUrlParam.get("method"));
      controllerUtil.addParam(url.substring(index + 1));
    }
  }

  public static int getContentLength(String line) {
    if(line.startsWith("Content-Length: ")){
      String contentLength = line.split(" ")[1];
      return Integer.parseInt(contentLength);
    }
    return 0;
  }


  public static void getRequestCookie(ControllerUtil controllerUtil, String line) {
    if(line.startsWith("Cookie: ")){
      controllerUtil.addRequestCookie( HttpRequestUtils.parseCookies(line.split(": ")[1]));
    }
  }

  public static void getContentType(ControllerUtil controllerUtil, String line) {
    if(line.startsWith("Accept: ")){
      int startIdx = line.indexOf(" ");
      int lastIdx = line.indexOf(",");
      String contentType = lastIdx == -1 ? line.substring(startIdx+ 1): line.substring(startIdx+ 1, lastIdx);
      controllerUtil.addContentType(contentType);
    }
  }
}
