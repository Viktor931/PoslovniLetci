package business.flyers.Exceptions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class RecaptchaServiceExceptionTest {
    @Test
    public void testException(){
        new RecaptchaServiceException("a", new Exception());
    }
}
