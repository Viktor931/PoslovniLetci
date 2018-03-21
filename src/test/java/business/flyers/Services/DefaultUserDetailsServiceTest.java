package business.flyers.Services;

import business.flyers.Constants.Constants;
import business.flyers.Entities.UserModel;
import business.flyers.Repositories.UserModelRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class DefaultUserDetailsServiceTest {
    private static String VALID_USERNAME = "validUsername";
    private static UserModel VALID_USER = Mockito.mock(UserModel.class);
    @InjectMocks
    private DefaultUserDetailsService defaultUserDetailsService;
    @Mock
    private UserModelRepository userModelRepository;

    @Before
    public void before(){
        when(userModelRepository.findOneByUsername(VALID_USERNAME)).thenReturn(VALID_USER);
        when(VALID_USER.getUsername()).thenReturn(VALID_USERNAME);
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
}
