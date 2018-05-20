package business.flyers.Services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import business.flyers.Exceptions.RecaptchaServiceException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RecaptchaServiceTest {
    @InjectMocks
    private RecaptchaService recaptchaService;
    @Mock
    private RestTemplate restTemplate;

    @Test(expected = RecaptchaServiceException.class)
    public void testRestTemplateCall(){
        recaptchaService.isResponseValid("", "");
        verify(restTemplate).postForEntity(any(), any(), any());
    }

    @Test
    public void testValidRecaptcha() {
    	ResponseEntity responseEntity = mock(ResponseEntity.class);
        when(restTemplate.postForEntity(any(String.class), any(MultiValueMap.class), any(Class.class))).thenReturn(responseEntity);
        when(responseEntity.getBody()).thenReturn(mock(RecaptchaService.RecaptchaResponse.class));
        recaptchaService.isResponseValid("", "");
    }
}
