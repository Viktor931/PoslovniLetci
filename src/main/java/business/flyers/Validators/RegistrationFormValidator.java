package business.flyers.Validators;

import business.flyers.Constants.Constants;
import business.flyers.Exceptions.RecaptchaServiceException;
import business.flyers.Services.RecaptchaService;
import business.flyers.dto.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RegistrationFormValidator implements Validator {
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    public boolean supports(Class<?> clazz) {
        return RegistrationForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegistrationForm form = (RegistrationForm) target;
        if(!form.getName().matches("^[ABCČĆDĐEFGHIJKLMNOPRSŠTUVZŽ]")){
            errors.reject(Constants.Registration.Error.NAME_CAPITAL_LETTER);
        }
        if(!form.getLastName().matches("^[ABCČĆDĐEFGHIJKLMNOPRSŠTUVZŽ]")){
            errors.reject(Constants.Registration.Error.LAST_NAME_CAPITAL_LETTER);
        }
        try{
            userDetailsService.loadUserByUsername(form.getUsername());
            errors.reject(Constants.Registration.Error.USERNAME_TAKEN);
        } catch (UsernameNotFoundException e){
            //left empty because if no username is found name is valid
        }
        if(!form.getPassword().equals(form.getPassword2())){
            errors.reject(Constants.Registration.Error.PASSWORDS_NOT_EQUAL);
        }

        if(!passwordValid(form.getPassword())){
            errors.reject(Constants.Registration.Error.PASSWORD_ERRORS);
        }
        //errors.reject(ERROR);//todo
    }

    private boolean passwordValid(String password){
        if(password.length() < 5 || password.length() > 15){
            return false;
        }
        int lowerCaseCounter = 0;
        int upperCaseCounter = 0;
        int digitCounter = 0;
        for(char c : password.toCharArray()){
            if(Character.isUpperCase(c)){
                upperCaseCounter++;
            }
            if(Character.isLowerCase(c)){
                lowerCaseCounter++;
            }
            if(Character.isUpperCase(c)){
                digitCounter++;
            }
        }
        return upperCaseCounter > 1 && lowerCaseCounter > 1 && digitCounter > 0;
    }
}