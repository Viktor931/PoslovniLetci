package business.flyers.Populators;

import business.flyers.Entities.UserModel;
import business.flyers.dto.RegistrationForm;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

@RunWith(MockitoJUnitRunner.class)
public class UserModelPopulatorTest {
    @InjectMocks
    private UserModelPopulator userModelPopulator;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void testCreateFromRegistrationForm(){
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setName("valid");
        registrationForm.setLastName("valid");
        registrationForm.setEmail("valid");
        registrationForm.setUsername("valid");
        registrationForm.setPassword("valid");
        registrationForm.setTwoStepLogin(true);
        UserModel userModel = new UserModel();
        userModelPopulator.populateFromRegistrationForm(registrationForm, userModel, passwordEncoder);
        Assert.assertEquals(userModel.getFirstName(), "valid");
        Assert.assertEquals(userModel.getLastName(), "valid");
        Assert.assertEquals(userModel.getUsername(), "valid");
        Assert.assertEquals(userModel.getEmail(), "valid");
        Assert.assertEquals(userModel.getPassword(), passwordEncoder.encode("valid"));
        Assert.assertEquals(userModel.getRawPassword(), "valid");
        Assert.assertEquals(userModel.getTwoStepLogin(), true);
    }
}
