package business.flyers.Controllers;

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
    @RequestMapping
    public ModelAndView registration(WebRequest request, Model model){
        ModelAndView result = new ModelAndView("login");
        return result;
    }
}
