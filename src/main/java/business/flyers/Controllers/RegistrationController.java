package business.flyers.Controllers;

import business.flyers.Services.DefaultUserDetailsService;
import business.flyers.dto.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = {"/registration"})
public class RegistrationController {
    @Autowired
    private DefaultUserDetailsService userDetailsService;

    @PostMapping(value = "/nameCheck")
    public @ResponseBody boolean registrationNameCheck(@RequestBody String name){
        try{
            userDetailsService.loadUserByUsername(name);
            return true;
        } catch(UsernameNotFoundException e) {
            return false;
        }
    }

    @RequestMapping
    public ModelAndView registration(WebRequest request, Model model){
        ModelAndView result = new ModelAndView("registration");
        return result;
    }
}
