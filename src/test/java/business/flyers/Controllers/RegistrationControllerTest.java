package business.flyers.Controllers;

import business.flyers.Services.DefaultUserDetailsService;
import business.flyers.Services.RecaptchaService;
import business.flyers.Validators.RegistrationFormValidator;
import business.flyers.dto.RegistrationForm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static org.mockito.Mockito.times;
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
    @Value("${recaptcha.site-key}")
    private static final String recaptchaKey = "";

    @Before
    public void init(){
        when(userDetailsService.loadUserByUsername("InvalidUsername")).thenThrow(UsernameNotFoundException.class);
        when(userDetailsService.loadUserByUsername("ValidUsername")).thenReturn(Mockito.mock(UserDetails.class));
        when(recaptchaService.isResponseValid(httpServletRequest.getRemoteAddr(), "ValidResponse")).thenReturn(true);
        when(recaptchaService.isResponseValid(httpServletRequest.getRemoteAddr(), "InvalidResponse")).thenReturn(false);
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
        Mockito.verify(webDataBinder, times(1)).addValidators(registrationFormValidator);
    }
}
