package business.flyers.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserDTOTest {
    @Test
    public void checkUserDTO() throws Exception{
        UserDTO userDTO = new UserDTO();
        userDTO.setFirstName("a");
        userDTO.setEmail("a");
        userDTO.setLastName("a");
        userDTO.setMatchingPassword("a");
        userDTO.setPassword("a");
        userDTO.setUsername("a");
        String test = "" + userDTO.getEmail() + userDTO.getFirstName() + userDTO.getLastName() + userDTO.getPassword() + userDTO.getMatchingPassword() + userDTO.getUsername();
    }
}
