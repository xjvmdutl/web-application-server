package util;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

import java.util.Map;
import java.util.Optional;
import org.junit.Test;
import webserver.UrlParam;

public class UrlUtilsTest {

  @Test
  public void URL_읽어_오기_성공_테스트() {
    String line = "GET /index.html HTTP/1.1";
    String url = UrlUtils.getFirstLine(line);
    assertEquals("/index.html", url);
  }

  @Test(expected = IllegalArgumentException.class)
  public void URL_읽기를_실패한_경우() throws Exception {
    String line = "Host: localhost:8080";
    UrlUtils.getRequestUrl(line);
  }

  @Test
  public void Url이_있고_Param이_없는_경우() {
    String url = "/index.html";
    UrlParam contentFromUrl = UrlUtils.getDivideContentFromUrl(url);
    assertEquals(contentFromUrl.getUrl(), "/index.html");
    assertEquals(contentFromUrl.getParams().size(), 0);
  }

  @Test
  public void Url이_있고_Param이_있는_경우() {
    String url = "/user/create?userId=javajigi&password=password&name=JaeSung";
    UrlParam contentFromUrl = UrlUtils.getDivideContentFromUrl(url);
    Map<String, String> params = contentFromUrl.getParams();
    assertEquals(contentFromUrl.getUrl(), "/user/create");
    assertEquals(params.get("userId"), "javajigi");
    assertEquals(params.get("password"), "password");
    assertEquals(params.get("name"), "JaeSung");
  }
}