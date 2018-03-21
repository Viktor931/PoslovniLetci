package business.flyers.Controllers;

import business.flyers.Services.DefaultUserDetailsService;
import business.flyers.Services.PaginationService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PrivatnoControllerTest {
    @InjectMocks
    private PrivatnoController privatnoController;
    @Mock
    private DefaultUserDetailsService userDetailsService;
    @Mock
    private PaginationService paginationService;

    @Test
    public void testPrivatnoRequest(){
        Assert.assertEquals(privatnoController.privatno("1").getViewName(), "privatno");
        Assert.assertEquals(privatnoController.privatno("1").getModel().get("users"), userDetailsService.getUsers(0, 10));
        Assert.assertEquals(privatnoController.privatno("1").getModel().get("page"), "1");
        Assert.assertEquals(privatnoController.privatno("1").getModel().get("maxPages"), paginationService.maxPages(userDetailsService.countUsers()));
    }
}
