package business.flyers.dto;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import business.flyers.Entities.UserModel;

@RunWith(MockitoJUnitRunner.class)
public class DefaultUserDetailsTest {
    @Test
    public void testInterfaceImplementation(){
        Assert.assertEquals(new DefaultUserDetails(new UserModel()) instanceof UserDetails, true);
    }
    @Test
    public void testUserDetails(){
        UserModel userModel = new UserModel();
        userModel.setUserGroup("a");
        userModel.setUsername("a");
        userModel.setPassword("a");
        userModel.setEmail("a");
        DefaultUserDetails userDetails = new DefaultUserDetails(userModel);
        Assert.assertEquals(((List<SimpleGrantedAuthority>)userDetails.getAuthorities()).get(0).getAuthority(), "ROLE_" + userModel.getUserGroup());
        Assert.assertEquals(userDetails.getPassword(), userModel.getPassword());
        Assert.assertEquals(userDetails.getUsername(), userModel.getUsername());
        Assert.assertEquals(userDetails.getUserModel().getEmail(), userModel.getEmail());
        Assert.assertEquals(userDetails.isAccountNonExpired() && userDetails.isAccountNonLocked() && userDetails.isCredentialsNonExpired() && userDetails.isEnabled(), true);
    }
}
