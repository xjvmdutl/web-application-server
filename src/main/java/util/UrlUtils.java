package util;

import java.util.StringTokenizer;

public class UrlUtils {

  private static int FIRST_LINE = 1;
  private static int URL_LINE_LENGTH = 3;

  public static String getURL(String line) {
    StringTokenizer tokenizer = new StringTokenizer(line," ");
    String method = tokenizer.nextToken();
    String url = tokenizer.nextToken();
    String httpVersion = tokenizer.nextToken();
    return url;
  }

  public static boolean iUrlContainsLine(int lineCount, String line) {
    String[] strs = line.split(" ");
    return FIRST_LINE == lineCount && strs.length == URL_LINE_LENGTH;
  }
}
