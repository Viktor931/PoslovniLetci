package business.flyers.Entities;

import business.flyers.dto.UserDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserModelTest {
    @Test
    public void checkUserModel() throws Exception{
        UserModel userModel = new UserModel();
        userModel.setFirstName("a");
        userModel.setEmail("a");
        userModel.setLastName("a");
        userModel.setPassword("a");
        userModel.setUsername("a");
        userModel.setUserGroup("a");
        userModel.setId(1L);
        String test = "" + userModel.getEmail() + userModel.getFirstName() + userModel.getLastName() + userModel.getPassword() + userModel.getUsername() + userModel.getUserGroup() + userModel.getId();
    }
}
