package business.flyers.Controllers;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import business.flyers.Entities.UserModel;
import business.flyers.Services.DefaultUserDetailsService;
import business.flyers.Services.RecaptchaService;
import business.flyers.Validators.RegistrationFormValidator;
import business.flyers.dto.RegistrationForm;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RegistrationControllerTest {
    @InjectMocks
    private RegistrationController registrationController;
    @Mock
    private DefaultUserDetailsService userDetailsService;
    @Mock
    private RegistrationFormValidator registrationFormValidator;
    @Mock
    private RecaptchaService recaptchaService;
    @Mock
    private HttpServletRequest httpServletRequest;
    @Mock
    private AuthenticationManager authenticationManager;

    @Value("${recaptcha.site-key}")
    private static final String recaptchaKey = "";

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private UserModel userForValidationTest = mock(UserModel.class);

    @Before
    public void init(){
        when(userDetailsService.loadUserByUsername("InvalidUsername")).thenThrow(UsernameNotFoundException.class);
        when(userDetailsService.loadUserByUsername("ValidUsername")).thenReturn(Mockito.mock(UserDetails.class));
        when(recaptchaService.isResponseValid(httpServletRequest.getRemoteAddr(), "ValidResponse")).thenReturn(true);
        when(recaptchaService.isResponseValid(httpServletRequest.getRemoteAddr(), "InvalidResponse")).thenReturn(false);
        when(userDetailsService.loadUserByActivationKey(httpServletRequest, "invalidKey")).thenThrow(UsernameNotFoundException.class);
    }

    @Test
    public void testRecaptchaSiteKey(){
        Assert.assertEquals(registrationController.getRecaptchaSiteKey(recaptchaKey), recaptchaKey);
    }

    @Test
    public void testRegistrationNameCheck(){
        Assert.assertFalse(registrationController.registrationNameCheck("InvalidUsername"));
        Assert.assertTrue(registrationController.registrationNameCheck("ValidUsername"));
    }

    @Test
    public void testGetRegistration(){
        ModelAndView modelAndView = registrationController.registration(Mockito.mock(WebRequest.class), Mockito.mock(Model.class));
        Assert.assertTrue(modelAndView.getViewName().equals("registration"));
        Assert.assertTrue(modelAndView.getModel().containsKey("registrationForm"));
        checkErrorMessages(modelAndView);
    }

    @Test
    public void testPostRegistration(){
        Assert.assertTrue(registrationController.verifyRegistration(Mockito.mock(WebRequest.class), Mockito.mock(Model.class), new RegistrationForm(), "ValidResponse").getViewName().equals("home"));
        verify(userDetailsService, times(1)).createUserFromRegistrationForm(any());

        ModelAndView invalidModelAndView = registrationController.verifyRegistration(Mockito.mock(WebRequest.class), Mockito.mock(Model.class), new RegistrationForm(), "InvalidResponse");
        Assert.assertTrue(invalidModelAndView.getViewName().equals("registration"));
        Assert.assertTrue(invalidModelAndView.getModel().containsKey("registrationForm"));
        checkErrorMessages(invalidModelAndView);
    }

    private void checkErrorMessages(ModelAndView invalidModelAndView) {
        Assert.assertTrue(invalidModelAndView.getModel().containsKey("usernameTaken"));
        Assert.assertTrue(invalidModelAndView.getModel().containsKey("passwordsNotEqual"));
        Assert.assertTrue(invalidModelAndView.getModel().containsKey("nameCapitalLetter"));
        Assert.assertTrue(invalidModelAndView.getModel().containsKey("lastNameCapitalLetter"));
        Assert.assertTrue(invalidModelAndView.getModel().containsKey("passwordErrors"));
    }

    @Test
    public void testInitDatabinder(){
        WebDataBinder webDataBinder = Mockito.mock(WebDataBinder.class);
        registrationController.initBinder(webDataBinder);
        verify(webDataBinder, times(1)).addValidators(registrationFormValidator);
    }

    @Test
    public void testInvalidActivationKey(){
        exception.expect(UsernameNotFoundException.class);
        registrationController.activate(httpServletRequest, "invalidKey");

    }

    @Test
    public void testValidActivationKey(){
        Assert.assertEquals(registrationController.activate(httpServletRequest, "validKey").getViewName(), "home");
    }
}
