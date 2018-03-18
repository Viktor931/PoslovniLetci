package business.flyers.Validators;

import business.flyers.Exceptions.RecaptchaServiceException;
import business.flyers.Services.RecaptchaService;
import business.flyers.dto.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;

@Component
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RegistrationFormValidator implements Validator {

    private static final String ERROR = "dawdawda";
    @Override
    public boolean supports(Class<?> clazz) {
        return RegistrationForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegistrationForm form = (RegistrationForm) target;
        //errors.reject(ERROR);//todo
    }
}