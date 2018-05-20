package business.flyers.Exception;

import business.flyers.Exceptions.RecaptchaServiceException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestClientException;

@RunWith(MockitoJUnitRunner.class)
public class RecaptchaServiceExceptionTest {
    @Test
    public void test(){
        RecaptchaServiceException e = new RecaptchaServiceException("a", new RestClientException("a"));
    }
}
