package business.flyers.Controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {
    @Autowired
    private RegistrationController registrationController;
    @Autowired
    private MockMvc mockMvc;

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
    }
}