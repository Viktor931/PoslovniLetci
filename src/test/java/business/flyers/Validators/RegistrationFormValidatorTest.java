package business.flyers.Validators;

import business.flyers.dto.RegistrationForm;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationFormValidatorTest {
    @Autowired
    private RegistrationFormValidator registrationFormValidator;

    @Test
    public void testSupportedClass(){
        Assert.assertEquals(registrationFormValidator.supports(RegistrationForm.class), true);
    }
    @Test
    public void testValidForm(){
        RegistrationForm registrationForm = new RegistrationForm();
        Errors errors = new BeanPropertyBindingResult(registrationForm, "registrationForm");
        registrationFormValidator.validate(registrationForm, errors);
        assertFalse(errors.hasErrors());
    }
}
