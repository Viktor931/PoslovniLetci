package business.flyers.Controllers;

import business.flyers.Exceptions.RecaptchaServiceException;
import business.flyers.Services.DefaultUserDetailsService;
import business.flyers.Services.RecaptchaService;
import business.flyers.Validators.RegistrationFormValidator;
import business.flyers.dto.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

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
        ModelAndView result = new ModelAndView("registration");
        result.addObject("registrationForm", new RegistrationForm());
        return result;
    }

    @PostMapping
    public ModelAndView verifyRegistration(WebRequest request, Model model, @ModelAttribute("form") RegistrationForm registrationForm, @RequestParam(name="g-recaptcha-response") String recaptchaResponse){
        if(recaptchaService.isResponseValid(httpServletRequest.getRemoteAddr(), recaptchaResponse)){
            return new ModelAndView("home");
        } else {
            registrationForm.setPassword("");
            registrationForm.setPassword2("");
            return new ModelAndView("registration", "registrationForm", registrationForm);
        }
    }
}