package business.flyers.Services;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import business.flyers.Constants.Constants;
import business.flyers.Entities.UserModel;
import business.flyers.Populators.UserModelPopulator;
import business.flyers.Repositories.UserModelRepository;
import business.flyers.dto.RegistrationForm;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
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
    @Mock
	private PasswordEncoder passwordEncoder;

    @Before
    public void before(){
        when(userModelRepository.findOneByUsername(VALID_USERNAME)).thenReturn(VALID_USER);
        when(VALID_USER.getUsername()).thenReturn(VALID_USERNAME);
        when(userModelRepository.findOneByActivationKey("validKey")).thenReturn(VALID_USER);
        when(VALID_USER.getSignUpDate()).thenReturn(LocalDateTime.now());
        when(userModelRepository.findOneByActivationKey("invalidKey")).thenReturn(null);
        when(userModelRepository.findOneByLoginKey("validKey")).thenReturn(VALID_USER);
        when(VALID_USER.getLoginTime()).thenReturn(LocalDateTime.now());
        when(userModelRepository.findOneByLoginKey("invalidKey")).thenReturn(null);
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
    public void testValidActivation(){
        defaultUserDetailsService.loadUserByActivationKey(mock(HttpServletRequest.class), "validKey");
        Assert.assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        Assert.assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testInvalidActivation(){
        defaultUserDetailsService.loadUserByActivationKey(mock(HttpServletRequest.class), "invalidKey");
    }

    @Test
    public void loadUserByLoginKey() {
        defaultUserDetailsService.loadUserByLoginKey(mock(HttpServletRequest.class), "validKey");
        Assert.assertNotNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByInvalidLoginKey(){
        defaultUserDetailsService.loadUserByLoginKey(mock(HttpServletRequest.class), "invalidKey");
    }

    @Test
    public void createLoginKey(){
        UserModel userModel = mock(UserModel.class);
        defaultUserDetailsService.createLoginKey(userModel);
        verify(userModelRepository, times(1)).save(userModel);
        verify(emailService, times(1)).sendEmail(any(), any(), any());
    }

    @Test
    public void saveUser() {
        defaultUserDetailsService.saveUser(new UserModel());
        verify(userModelRepository).save(any());
    }

    @Test
	public void testResetPassword() {
    	UserModel userModel = mock(UserModel.class);
    	when(userModelRepository.findOneByUsername("a")).thenReturn(userModel);
		defaultUserDetailsService.resetPassword("a");
    	verify(userModelRepository).findOneByUsername("a");
    	verify(userModel).setPassword(any());
    	verify(userModelRepository).save(userModel);
    	verify(emailService).sendEmail(any(), any(), any());
	}
}