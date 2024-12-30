package util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class UrlParseUtilsTest  {

    @Test
    public void urlParseTest() {
        String indexLine = "GET /index.html HTTP/1.1";
        String testLine = "GET /test HTTP/1.1";

        String index = UrlParseUtils.parseUrl(indexLine);
        String test = UrlParseUtils.parseUrl(testLine);
        assertEquals(index, "/index.html");
        assertEquals(test, "/test");
    }

    @Test
    public void separateUrlTest() {
        String request = "/user/create?userId=javajigi&password=password&name=JaeSung";
        String request2 = "/index.html";
        String result = UrlParseUtils.separateUrl(request);
        String result2 = UrlParseUtils.separateUrl(request2);

        assertEquals(result, "/user/create");
        assertEquals(result2, "/index.html");
    }

    @Test
    public void separateParamTest() {
        String request = "/user/create?userId=javajigi&password=password&name=JaeSung";
        String request2 = "/index.html";

        String result = UrlParseUtils.separateParam(request);
        String result2 = UrlParseUtils.separateParam(request2);

        assertEquals(result, "userId=javajigi&password=password&name=JaeSung");
        assertEquals(result2, "");
    }
    @Test
    public void methodParseTest() {
        String getLine = "GET /index.html HTTP/1.1";
        String postLine = "POST /user/create HTTP/1.1";

        String getMethod = UrlParseUtils.parseMethod(getLine);
        String postMethod = UrlParseUtils.parseMethod(postLine);
        assertEquals(getMethod, "GET");
        assertEquals(postMethod, "POST");
    }
}
