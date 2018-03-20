package business.flyers.Validators;

import business.flyers.Constants.Constants;
import business.flyers.dto.RegistrationForm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationFormValidatorTest {
    @InjectMocks
    private RegistrationFormValidator registrationFormValidator;
    @Mock
    private UserDetailsService userDetailsService;

    @Before
    public void setup(){
        when(userDetailsService.loadUserByUsername("invalidUsername")).thenReturn(Mockito.mock(UserDetails.class));
        when(userDetailsService.loadUserByUsername("username")).thenThrow(UsernameNotFoundException.class);
    }

    @Test
    public void testSupportedClass(){
        Assert.assertEquals(registrationFormValidator.supports(RegistrationForm.class), true);
    }

    @Test
    public void testValidForm(){
        RegistrationForm registrationForm = whenForm("A", "A", "username", "aaAA1", "aaAA1");
        Errors errors = new BeanPropertyBindingResult(registrationForm, "registrationForm");
        registrationFormValidator.validate(registrationForm, errors);
        assertFalse(errors.hasErrors());
    }

    @Test
    public void testInvalidForm(){
        RegistrationForm registrationForm = whenForm("name", "lastName", "username", "password", "password");
        Errors errors = new BeanPropertyBindingResult(registrationForm, "registrationForm");
        registrationFormValidator.validate(registrationForm, errors);
        assertTrue(errors.hasErrors());

        registrationForm = whenForm("name", "Lastname", "username", "aaAA1", "aaAA1");
        checkForError(registrationForm, Constants.Registration.Error.NAME_CAPITAL_LETTER);
        registrationForm = whenForm("Name", "lastname", "username", "aaAA1", "aaAA1");
        checkForError(registrationForm, Constants.Registration.Error.LAST_NAME_CAPITAL_LETTER);
        registrationForm = whenForm("Name", "Lastname", "invalidUsername", "aaAA1", "aaAA1");
        checkForError(registrationForm, Constants.Registration.Error.USERNAME_TAKEN);
        registrationForm = whenForm("Name", "Lastname", "username", "aaAA1", "aaAA1a");
        checkForError(registrationForm, Constants.Registration.Error.PASSWORDS_NOT_EQUAL);
        registrationForm = whenForm("Name", "Lastname", "username", "aaAA", "aaAA1");
        checkForError(registrationForm, Constants.Registration.Error.PASSWORD_ERRORS);
        registrationForm = whenForm("Name", "Lastname", "username", "aaAa1", "aaAA1");
        checkForError(registrationForm, Constants.Registration.Error.PASSWORD_ERRORS);
        registrationForm = whenForm("Name", "Lastname", "username", "aAAA1", "aaAA1");
        checkForError(registrationForm, Constants.Registration.Error.PASSWORD_ERRORS);
        registrationForm = whenForm("Name", "Lastname", "username", "aaAA1aaaaaaaaaaa", "aaAA1");
        checkForError(registrationForm, Constants.Registration.Error.PASSWORD_ERRORS);
    }

    private void checkForError(RegistrationForm registrationForm, String errorMessage) {
        Errors errors = new BeanPropertyBindingResult(registrationForm, "registrationForm");
        registrationFormValidator.validate(registrationForm, errors);
        boolean found = false;
        for(ObjectError error : errors.getAllErrors()){
            if(errorMessage.equals(error.getCode())){
                found = true;
            }
        }
        assertTrue(found);
    }

    private RegistrationForm whenForm(String name, String lastName, String username, String password, String password2) {
        RegistrationForm registrationForm = Mockito.mock(RegistrationForm.class);
        when(registrationForm.getName()).thenReturn(name);
        when(registrationForm.getLastName()).thenReturn(lastName);
        when(registrationForm.getUsername()).thenReturn(username);
        when(registrationForm.getPassword()).thenReturn(password);
        when(registrationForm.getPassword2()).thenReturn(password2);
        return registrationForm;
    }
}
