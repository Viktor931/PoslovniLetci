package business.flyers.Entities;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static business.flyers.Constants.Constants.Login.MAX_FAILED_ATTEMPTS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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
        String test = "" + userModel.getEmail() + userModel.getFirstName() + userModel.getLastName() + userModel.getPassword() +
                userModel.getUsername() + userModel.getUserGroup() + userModel.getId() + userModel.getRawPassword() +
                userModel.getTwoStepLogin() + userModel.getLoginKey() + userModel.getSignUpDate() + userModel.getLoginTime();

        userModel.setActivationKey("key");
        Assert.assertFalse(userModel.isActivated()); //activation key isnt null
    }

    @Test
    public void testLoginLock(){
        for(int i = 0; i < MAX_FAILED_ATTEMPTS; i++){
            userModel.failedLogin();
        }
        assertTrue(userModel.isLocked());
        userModel.successfulLogin();
        assertFalse(userModel.isLocked());
    }
}
