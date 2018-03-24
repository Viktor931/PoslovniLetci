package business.flyers.Entities;

import business.flyers.Constants.Constants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@RunWith(MockitoJUnitRunner.class)
public class UserModelTest {
    @InjectMocks
    private UserModel userModel;
    @Mock
    private PasswordEncoder passwordEncoder;

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
        userModel.setRawPassword("a");
        String test = "" + userModel.getEmail() + userModel.getFirstName() + userModel.getLastName() + userModel.getPassword() +
                userModel.getUsername() + userModel.getUserGroup() + userModel.getId() + userModel.getRawPassword();

        userModel.setActivationKey("key");
        Assert.assertFalse(userModel.isValid()); //activation key isnt null
        LocalDateTime oldTime = LocalDateTime.now().minusHours(Constants.Registration.Activation.TIMEOUT_IN_HOURS);
        userModel.setSignUpDate(oldTime);
        Assert.assertFalse(userModel.isValid()); //signupdate too old
        userModel.setSignUpDate(LocalDateTime.now());
        Assert.assertFalse(userModel.validateActivationKey("wrongKey"));
        Assert.assertTrue(userModel.validateActivationKey("key"));
        Assert.assertTrue(userModel.isValid());
    }
}
