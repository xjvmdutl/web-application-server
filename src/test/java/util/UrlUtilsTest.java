package util;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class UrlUtilsTest {

  @Test
  public void URL_읽어_오기_성공_테스트() {
    //given
    String line = "GET /index.html HTTP/1.1";

    //when
    Map<String, String> reqeuests = UrlUtils.getFirstLine(line);

    //then
    assertEquals("GET", reqeuests.get("method"));
    assertEquals("/index.html", reqeuests.get("url"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void URL_읽기를_실패한_경우() throws Exception {
    String line = "Host: localhost:8080";
    UrlUtils.getFirstLine(line);
  }

  @Test
  public void Url이_있고_Param이_없는_경우() {
    //Given
    ControllerUtil controllerUtil = new ControllerUtil();
    Map<String, String> requests = new HashMap<>();
    requests.put("url", "/index.html");
    requests.put("method", "GET");

    //when
    UrlUtils.getDivideContentFromUrl(controllerUtil, requests);

    assertEquals(controllerUtil.getRequestMethod(), "GET");
    assertEquals(controllerUtil.getResponseUrl(), "/index.html");
    assertEquals(controllerUtil.getParams().size(), 0);
  }

  @Test
  public void Url이_있고_Param이_있는_경우() {
    //Given
    ControllerUtil controllerUtil = new ControllerUtil();
    Map<String, String> requests = new HashMap<>();
    requests.put("url", "/user/create?userId=javajigi&password=password&name=JaeSung");
    requests.put("method", "POST");

    UrlUtils.getDivideContentFromUrl(controllerUtil, requests);

    assertEquals(controllerUtil.getRequestMethod(), "POST");
    assertEquals(controllerUtil.getRequestUrl(), "/user/create");
    Map<String, String> params = controllerUtil.getParams();
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