package business.flyers.Controllers.Integration;

import business.flyers.Controllers.RegistrationController;
import business.flyers.Services.RecaptchaService;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerIntegrationTest {
    @Autowired
    private RegistrationController registrationController;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private RecaptchaService recaptchaService;

    @Test
    public void contexLoads() throws Exception {
        assertThat(registrationController).isNotNull();
    }
    @Test
    public void testAjaxNameCheck() throws Exception {
        this.mockMvc.perform(post("/registration/nameCheck").contentType(MediaType.APPLICATION_JSON).content("admin")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("true")));
        this.mockMvc.perform(post("/registration/nameCheck").contentType(MediaType.APPLICATION_JSON).content("invalidName")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().string(containsString("false")));
    }
    @Test
    public void shouldReturnDefaultMessage() throws Exception {
        this.mockMvc.perform(get("/registration")).andDo(print()).andExpect(status().isOk());
        this.mockMvc.perform(get("/registration")).andReturn().getResponse().getForwardedUrl().equals("/WEB-INF/view/registration.jsp");
        this.mockMvc.perform(get("/registration")).andExpect(model().attribute("registrationForm", notNullValue()));
    }
    @Test
    public void testRecaptchaInvalidFormSubmission() throws Exception {
        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("name", "A"),
                        new BasicNameValuePair("lastName", "A"),
                        new BasicNameValuePair("username", "A"),
                        new BasicNameValuePair("email", "a@a.a"),
                        new BasicNameValuePair("password", "aaAA1"),
                        new BasicNameValuePair("password2", "aaAA1"),
                        new BasicNameValuePair("twoStepLogin", "true"),
                        new BasicNameValuePair("g-recaptcha-response", "a")
                ))))).andReturn().getResponse().getForwardedUrl().equals("/WEB-INF/view/registration.jsp");
        this.mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("name", "A"),
                        new BasicNameValuePair("lastName", "A"),
                        new BasicNameValuePair("username", "A"),
                        new BasicNameValuePair("email", "a@a.a"),
                        new BasicNameValuePair("password", "aaAA1"),
                        new BasicNameValuePair("password2", "aaAA1"),
                        new BasicNameValuePair("twoStepLogin", "true"),
                        new BasicNameValuePair("g-recaptcha-response", "a")
                ))))).andExpect(model().attribute("registrationForm", notNullValue()));
    }

    @Test
    public void testRecaptchaValidFormSubmission() throws Exception {
        mockMvc.perform(post("/registration")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .content(EntityUtils.toString(new UrlEncodedFormEntity(Arrays.asList(
                        new BasicNameValuePair("name", "A"),
                        new BasicNameValuePair("lastName", "A"),
                        new BasicNameValuePair("username", "A"),
                        new BasicNameValuePair("email", "a@a.a"),
                        new BasicNameValuePair("password", "aaAA1"),
                        new BasicNameValuePair("password2", "aaAA1"),
                        new BasicNameValuePair("twoStepLogin", "true"),
                        new BasicNameValuePair("g-recaptcha-response", "a")
                ))))).andReturn().getResponse().getForwardedUrl().equals("/WEB-INF/view/home.jsp");
    }
}