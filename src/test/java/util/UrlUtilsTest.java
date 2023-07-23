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
    UrlParam urlParam = UrlUtils.getFirstLine(line);
    assertEquals("GET", urlParam.getMethod());
    assertEquals("/index.html", urlParam.getUrl());
  }

  @Test(expected = IllegalArgumentException.class)
  public void URL_읽기를_실패한_경우() throws Exception {
    String line = "Host: localhost:8080";
    UrlUtils.getFirstLine(line);
  }

  @Test
  public void Url이_있고_Param이_없는_경우() {
    UrlParam urlParam = new UrlParam("GET", "/index.html");
    UrlParam contentFromUrl = UrlUtils.getDivideContentFromUrl(urlParam);
    assertEquals(contentFromUrl.getMethod(), "GET");
    assertEquals(contentFromUrl.getUrl(), "/index.html");
    assertEquals(contentFromUrl.getParams().size(), 0);
  }

  @Test
  public void Url이_있고_Param이_있는_경우() {
    UrlParam urlParam = new UrlParam("POST",
        "/user/create?userId=javajigi&password=password&name=JaeSung");

    UrlParam contentFromUrl = UrlUtils.getDivideContentFromUrl(urlParam);
    assertEquals(contentFromUrl.getMethod(), "POST");
    assertEquals(contentFromUrl.getUrl(), "/user/create");
    Map<String, String> params = contentFromUrl.getParams();
    assertEquals(params.get("userId"), "javajigi");
    assertEquals(params.get("password"), "password");
    assertEquals(params.get("name"), "JaeSung");
  }


  @Test
  public void 읽어온_라인이_ContentLength가_아닐경우() {
    String line = "POST /user/create?userId=javajigi&password=password&name=JaeSung HTTP/1.1";
    int contentLength = UrlUtils.getContentLength(line);

    assertEquals(contentLength, 0);
  }

  @Test
  public void 읽어온_라인이_ContentLength이고_경우() {
    String line = "Content-Length: 59";
    int contentLength = UrlUtils.getContentLength(line);

    assertEquals(contentLength, 59);
  }

  @Test
  public void 읽어온_라인이_마지막_라인일때() {
    String line = "Content-Length: 59";
    int contentLength = UrlUtils.getContentLength(line);

    assertEquals(contentLength, 59);
  }

  @Test
  public void 읽어온_라인이_마지막_라인이_아닐때() {
    String line = "Content-Length: 59";
    int contentLength = UrlUtils.getContentLength(line);

    assertEquals(contentLength, 59);
  }
}