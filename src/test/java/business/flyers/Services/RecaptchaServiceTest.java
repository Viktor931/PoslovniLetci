package business.flyers.Services;

import business.flyers.Exceptions.RecaptchaServiceException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RecaptchaServiceTest {
    @Autowired
    private RecaptchaService recaptchaService;
    @Autowired
    private RestTemplate restTemplate;

    @Test
    public void testInvalidResponse(){
        ReflectionTestUtils.setField(recaptchaService, "restTemplate", restTemplate);
        Assert.assertEquals(recaptchaService.isResponseValid("192.192.192.192", "aaa"), false);
    }
    @Test
    public void testValidResponse(){
        ReflectionTestUtils.setField(recaptchaService, "restTemplate", restTemplate);
        ReflectionTestUtils.setField(recaptchaService, "recaptchaSecretKey", "6LeIxAcTAAAAAGG-vFI1TnRWxMZNFuojJ4WifJWe");
        Assert.assertEquals(recaptchaService.isResponseValid("192.192.192.192", "aaa"), true);
    }
    @Test(expected = RecaptchaServiceException.class)
    public void testUnavailableRecaptchaAPI(){
        ReflectionTestUtils.setField(recaptchaService, "restTemplate", null);
        recaptchaService.isResponseValid("192.192.192.192", "aa");
        ReflectionTestUtils.setField(recaptchaService, "restTemplate", restTemplate);
    }
}
