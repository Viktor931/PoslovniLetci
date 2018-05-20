package business.flyers.dto;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationFormTest {
    @Test
    public void testUserDetails(){
        RegistrationForm registrationForm = new RegistrationForm();
        registrationForm.setName("a");
        registrationForm.setLastName("a");
        registrationForm.setUsername("a");
        registrationForm.setEmail("a");
        registrationForm.setPassword2("a");
        registrationForm.setPassword("a");
        registrationForm.setTwoStepLogin(false);
        String test = "" + registrationForm.getName() + registrationForm.getLastName() + registrationForm.getUsername() +
                registrationForm.getEmail() + registrationForm.getPassword() + registrationForm.getPassword2() + registrationForm.getTwoStepLogin();
    }
}
