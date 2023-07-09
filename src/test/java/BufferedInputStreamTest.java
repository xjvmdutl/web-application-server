import static junit.framework.TestCase.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

public class BufferedInputStreamTest {

  private BufferedReader bufferedReader;
  private String line;
  @Before
  public void setup(){
    line = null;
    String data = "GET /index.html HTTP/1.1\nHost: localhost:8080\nConnection: keep-alive";
    bufferedReader = new BufferedReader(new StringReader(data));
  }

  @Test
  public void bufferedReaderTest() throws IOException {
    //Given
    List<String> lines = new ArrayList<>();

    //When
    while (!"".equals(line = bufferedReader.readLine()) ){
      if(line == null)
        break;
      lines.add(line);
    }

    //then
    assertEquals(lines, Arrays.asList("GET /index.html HTTP/1.1", "Host: localhost:8080", "Connection: keep-alive"));
  }

  @Test
  public void 첫번째_라인에서_요청_URL_추출() throws IOException {
    //Given
    int count = 0;

    //When
    while (!"".equals(line = bufferedReader.readLine()) ){

      if(line == null)
        break;
    }

    //then
  }
}
