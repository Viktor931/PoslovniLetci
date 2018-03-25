package business.flyers.Controllers;

import business.flyers.Services.DefaultUserDetailsService;
import business.flyers.dto.DefaultUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = {"/login"})
public class LoginController {
    @Autowired
    private DefaultUserDetailsService defaultUserDetailsService;

    @RequestMapping
    public ModelAndView getLoginPage(WebRequest request){
        ModelAndView result = new ModelAndView("login");
        return result;
    }

    @RequestMapping("/confirm")
    public ModelAndView confirmLogin(HttpServletRequest request,
                                     @RequestParam(defaultValue = "") String key) {
        defaultUserDetailsService.loadUserByLoginKey(request, key);
        return new ModelAndView("home");
    }
}
