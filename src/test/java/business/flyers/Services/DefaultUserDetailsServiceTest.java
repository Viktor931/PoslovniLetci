package business.flyers.Services;

import business.flyers.Entities.UserModel;
import business.flyers.Repositories.UserModelRepository;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class DefaultUserDetailsServiceTest {
    @Autowired
    private DefaultUserDetailsService defaultUserDetailsService;
    @Autowired
    private UserModelRepository userModelRepository;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testInvalidName(){
        thrown.expect(UsernameNotFoundException.class);
        defaultUserDetailsService.loadUserByUsername("aa");
    }

    @Test
    public void testNameValidation(){
        UserModel userModel = new UserModel();
        userModel.setUsername("testingusername");
        userModelRepository.save(userModel);
        Assert.assertEquals(defaultUserDetailsService.loadUserByUsername("testingusername").getUsername(), "testingusername");
    }
}
