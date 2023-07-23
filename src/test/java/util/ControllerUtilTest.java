package util;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class ControllerUtilTest {
  private ControllerUtil controllerUtil;


  @Test
  public void ResponseURL_user_create_검증_테스트(){
    controllerUtil = new ControllerUtil("POST", "/user/create");
    assertEquals(controllerUtil.getResponseUrl(), "/index.html");

  }
  @Test
  public void ResponseURL_디폴트_검증_테스트(){
    controllerUtil = new ControllerUtil("GET", "/index.html");
    assertEquals(controllerUtil.getResponseUrl(), "/index.html");
  }

}