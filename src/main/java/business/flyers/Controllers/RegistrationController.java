package business.flyers.Controllers;

import business.flyers.Constants.Constants;
import business.flyers.Entities.UserModel;
import business.flyers.Repositories.UserModelRepository;
import business.flyers.Services.DefaultUserDetailsService;
import business.flyers.Services.RecaptchaService;
import business.flyers.Validators.RegistrationFormValidator;
import business.flyers.dto.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.context.support.SecurityWebApplicationContextUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Controller
@RequestMapping(value = {"/registration"})
public class RegistrationController {
    @Autowired
    private DefaultUserDetailsService userDetailsService;
    @Autowired
    private RegistrationFormValidator registrationFormValidator;
    @Autowired
    private RecaptchaService recaptchaService;
    @Autowired
    private HttpServletRequest httpServletRequest;

    @ModelAttribute("recaptchaSiteKey")
    public String getRecaptchaSiteKey(@Value("${recaptcha.site-key}") String recaptchaSiteKey) {
        return recaptchaSiteKey;
    }

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(registrationFormValidator);
    }

    @PostMapping(value = "/nameCheck")
    public @ResponseBody boolean registrationNameCheck(@RequestBody String name){
        try{
            userDetailsService.loadUserByUsername(name);
            return true;
        } catch(UsernameNotFoundException e) {
            return false;
        }
    }

    @GetMapping
    public ModelAndView registration(WebRequest request, Model model){
        ModelAndView result = new ModelAndView("registration", "registrationForm", new RegistrationForm());
        populateErrorMessages(result);
        return result;
    }

    private void populateErrorMessages(ModelAndView result) {
        result.addObject("usernameTaken", Constants.Registration.Error.USERNAME_TAKEN);
        result.addObject("passwordsNotEqual", Constants.Registration.Error.PASSWORDS_NOT_EQUAL);
        result.addObject("nameCapitalLetter", Constants.Registration.Error.NAME_CAPITAL_LETTER);
        result.addObject("lastNameCapitalLetter", Constants.Registration.Error.LAST_NAME_CAPITAL_LETTER);
        result.addObject("passwordErrors", Constants.Registration.Error.PASSWORD_ERRORS);
    }

    @PostMapping
    public ModelAndView verifyRegistration(WebRequest request, Model model,
                                           @ModelAttribute("form") @Valid RegistrationForm registrationForm,
                                           @RequestParam(name="g-recaptcha-response") String recaptchaResponse){
        if(recaptchaService.isResponseValid(httpServletRequest.getRemoteAddr(), recaptchaResponse)){
            userDetailsService.createUserFromRegistrationForm(registrationForm);
            return new ModelAndView("home");
        } else {
            registrationForm.setPassword("");
            registrationForm.setPassword2("");
            ModelAndView result = new ModelAndView("registration", "registrationForm", registrationForm);
            populateErrorMessages(result);
            return result;
        }
    }

    @GetMapping(value = "/activation")
    public ModelAndView activate(HttpServletRequest request,
                         @RequestParam(defaultValue = "") String key) {
        UserModel user = userDetailsService.loadUserByActivationKey(request, key);
        return new ModelAndView("home");
    }
}