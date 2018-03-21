package business.flyers.Controllers;

import business.flyers.Constants.Constants;
import business.flyers.Services.DefaultUserDetailsService;
import business.flyers.Services.PaginationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PrivatnoController {
    @Autowired
    private DefaultUserDetailsService defaultUserDetailsService;
    @Autowired
    private PaginationService paginationService;

    @RequestMapping
    public ModelAndView privatno(@RequestParam(value = "page", defaultValue = "1") String page) {
        ModelAndView result = new ModelAndView("privatno");
        result.addObject("users", defaultUserDetailsService.getUsers(paginationService.from(page), paginationService.to(page)));
        result.addObject("page", page);
        result.addObject("maxPages", paginationService.maxPages(defaultUserDetailsService.countUsers()));
        return result;
    }
}
