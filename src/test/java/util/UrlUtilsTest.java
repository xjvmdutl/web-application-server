package util;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import org.junit.Test;

public class UrlUtilsTest {
  @Test
  public void URL_읽어_오기_테스트() {
    String line = "GET /index.html HTTP/1.1";
    String url = UrlUtils.getURL(line);

    assertEquals("/index.html", url);
  }

  @Test
  public void Url_포함_하는_라인인지() {
    String line = "GET /index.html HTTP/1.1";
    boolean iUrlContainsLine = UrlUtils.iUrlContainsLine(1, line);

    assertTrue(iUrlContainsLine);
  }

  @Test
  public void Url_포함하는_라인이_아닐_경우() {
    String line = "Host: localhost:8080";

    boolean iUrlContainsLine = UrlUtils.iUrlContainsLine(2, line);

    assertFalse(iUrlContainsLine);
  }
}