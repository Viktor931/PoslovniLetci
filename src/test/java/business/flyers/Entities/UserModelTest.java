package business.flyers.Entities;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
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
        userModel.setTwoStepLogin(true);
        userModel.setLoginKey("a");
        userModel.setSignUpDate(LocalDateTime.now());
        userModel.setLoginTime(LocalDateTime.now());
        userModel.failedLogin();
        String test = "" + userModel.getEmail() + userModel.getFirstName() + userModel.getLastName() + userModel.getPassword() +
                userModel.getUsername() + userModel.getUserGroup() + userModel.getId() + userModel.getRawPassword() +
                userModel.getTwoStepLogin() + userModel.getLoginKey() + userModel.getSignUpDate() + userModel.getLoginTime();

        userModel.setActivationKey("key");
        Assert.assertFalse(userModel.isActivated()); //activation key isnt null
    }
}
