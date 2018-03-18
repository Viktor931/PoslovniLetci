package business.flyers.Services;

import business.flyers.Exceptions.RecaptchaServiceException;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@Service
public class RecaptchaService {

    static class RecaptchaResponse {
        @JsonProperty("success")
        private boolean success;
        @JsonProperty("error-codes")
        private Collection<String> errorCodes;
    }

    @Autowired
    private RestTemplate restTemplate;

    @Value("${recaptcha.url}")
    private String recaptchaUrl;

    @Value("${recaptcha.secret-key}")
    private String recaptchaSecretKey;

    public boolean isResponseValid(String remoteIp, String response) {
        RecaptchaResponse recaptchaResponse;
        try {
            recaptchaResponse = restTemplate.postForEntity(recaptchaUrl, createBody(remoteIp, response), RecaptchaResponse.class).getBody();
        } catch (Exception e) {
            throw new RecaptchaServiceException("Recaptcha API not available due to exception", e);
        }
        return recaptchaResponse.success;
    }

    private MultiValueMap<String, String> createBody(String remoteIp, String response) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("secret", recaptchaSecretKey);
        form.add("remoteip", remoteIp);
        form.add("response", response);
        return form;
    }
}
