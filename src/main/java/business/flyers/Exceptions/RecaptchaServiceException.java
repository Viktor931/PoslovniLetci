package business.flyers.Exceptions;

import org.springframework.web.client.RestClientException;

public class RecaptchaServiceException extends RuntimeException {
    public RecaptchaServiceException(String s, Exception e) {
        super(s + e.getMessage());
    }
}
