package business.flyers.Controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.WebRequest;

import business.flyers.Services.DefaultUserDetailsService;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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

    @Test
    public void testForgottenPassword() {
    	loginController.forgottenPassword(mock(HttpServletResponse.class), "a");
    	verify(defaultUserDetailsService).resetPassword("a");
	}
}
