package business.flyers.Services;

import business.flyers.Constants.Constants;
import business.flyers.Entities.UserModel;
import business.flyers.Populators.UserModelPopulator;
import business.flyers.Repositories.UserModelRepository;
import business.flyers.dto.RegistrationForm;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.servlet.Registration;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
public class DefaultUserDetailsServiceTest {
    private static String VALID_USERNAME = "validUsername";
    private static UserModel VALID_USER = Mockito.mock(UserModel.class);
    @InjectMocks
    private DefaultUserDetailsService defaultUserDetailsService;
    @Mock
    private UserModelRepository userModelRepository;
    @Mock
    private EmailService emailService;
    @Mock
    private UserModelPopulator userModelPopulator;

    @Before
    public void before(){
        when(userModelRepository.findOneByUsername(VALID_USERNAME)).thenReturn(VALID_USER);
        when(VALID_USER.getUsername()).thenReturn(VALID_USERNAME);
        when(userModelRepository.findOneByActivationKey("validKey")).thenReturn(VALID_USER);
        when(userModelRepository.findOneByActivationKey("invalidKey")).thenReturn(null);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testInvalidName(){
        defaultUserDetailsService.loadUserByUsername("aa");
    }

    @Test
    public void testNameValidation(){
        Assert.assertEquals(defaultUserDetailsService.loadUserByUsername(VALID_USERNAME).getUsername(), VALID_USERNAME);
    }

    @Test
    public void testGetAllUsers(){
        Assert.assertEquals(defaultUserDetailsService.getUsers(0, Constants.Pagination.ENTRIES_PER_PAGE).size(),
                userModelRepository.findAll()
                .subList(0, userModelRepository.count() < Constants.Pagination.ENTRIES_PER_PAGE ? (int) userModelRepository.count() : Constants.Pagination.ENTRIES_PER_PAGE).size());
    }

    @Test
    public void testCountUsers(){
        Assert.assertEquals(defaultUserDetailsService.countUsers(), userModelRepository.count());
    }

    @Test
    public void createUser(){
        RegistrationForm registrationForm = Mockito.mock(RegistrationForm.class);
        defaultUserDetailsService.createUserFromRegistrationForm(registrationForm);
        verify(userModelRepository, times(1)).save(any());
        verify(userModelPopulator, times(1)).populateFromRegistrationForm(any(), any(), any());
        verify(emailService, times(1)).sendEmail(any(),any(),any());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByActivationKey(){
        Assert.assertEquals(defaultUserDetailsService.loadUserByActivationKey(mock(HttpServletRequest.class), "validKey"), VALID_USER);
        verify(userModelRepository, times(1)).save(any());
        defaultUserDetailsService.loadUserByUsername("invalidKey");
    }

    @Test
    public void testActivation(){
        defaultUserDetailsService.loadUserByActivationKey(mock(HttpServletRequest.class), "validKey");
        Assert.assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testInvalidActivation(){
        defaultUserDetailsService.loadUserByActivationKey(mock(HttpServletRequest.class), "invalidKey");
    }
}
