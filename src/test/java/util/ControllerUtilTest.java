package util;

import static org.junit.Assert.assertEquals;

import db.DataBase;
import java.io.IOException;
import javax.xml.crypto.Data;
import model.User;
import org.junit.Before;
import org.junit.Test;

public class ControllerUtilTest {

  private ControllerUtil controllerUtil;

  @Before
  public void init() {
    DataBase.clear();
    controllerUtil = new ControllerUtil();
  }


  @Test
  public void ResponseURL_디폴트_검증_테스트() throws IOException {
    controllerUtil.addUrlAndMethod("/index.html", "GET");
    controllerUtil.matchingUrl();

    assertEquals(controllerUtil.getResponseUrl(), "/index.html");
  }

  @Test
  public void 데이터_저장_테스트() {
    User user = new User("test1", "1234", "테스터1", "junhokim");

    controllerUtil.addUser(user);

    User findUser = DataBase.findUserById(user.getUserId());
    assertEquals(findUser.getUserId(), user.getUserId());
    assertEquals(findUser.getPassword(), user.getPassword());
    assertEquals(findUser.getEmail(), user.getEmail());
    assertEquals(findUser.getName(), user.getName());

  }

}