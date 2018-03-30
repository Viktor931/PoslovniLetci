package business.flyers.Controllers;

import business.flyers.Services.DefaultUserDetailsService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.WebRequest;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {
    @InjectMocks
    private LoginController loginController;
    @Mock
    private DefaultUserDetailsService defaultUserDetailsService;

    @Test
    public void getLoginPage() throws Exception {
        Assert.assertEquals(loginController.getLoginPage(mock(WebRequest.class)).getViewName(), "login");
    }

    @Test
    public void confirmLogiin() {
        Assert.assertEquals(loginController.confirmLogin(mock(HttpServletRequest.class), "key").getViewName(), "home");
        verify(defaultUserDetailsService, times(1)).loadUserByLoginKey(any(), any());
    }

    @Test
    public void testLogout(){
        SecurityContextHolder.getContext().setAuthentication(mock(Authentication.class));
        HttpServletResponse httpServletResponse = mock(HttpServletResponse.class);
        loginController.getLogout(httpServletResponse);
        verify(httpServletResponse).addCookie(any());
        Assert.assertNull(SecurityContextHolder.getContext().getAuthentication());
    }
}
