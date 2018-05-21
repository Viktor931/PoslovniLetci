package business.flyers.Security;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import business.flyers.Entities.UserModel;
import business.flyers.Services.DefaultUserDetailsService;
import business.flyers.dto.DefaultUserDetails;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider
{
	@Autowired
	private DefaultUserDetailsService userDetailsService;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String name = authentication.getName();
		String password = authentication.getCredentials().toString();
		UserModel userModel = ((DefaultUserDetails) userDetailsService.loadUserByUsername(name)).getUserModel();
		if(!passwordEncoder.matches(password, userModel.getPassword())){
			userModel.failedLogin();
			userDetailsService.saveUser(userModel);
			throw new BadCredentialsException("Invalid login info");
		}
		if(userModel.isLocked()){
			throw new BadCredentialsException("Invalid login info");
		}
		if(userModel.getTwoStepLogin()){
			userDetailsService.createLoginKey(userModel);
			throw new InsufficientAuthenticationException("");
		}
		userModel.successfulLogin();
		userDetailsService.saveUser(userModel);
		return new UsernamePasswordAuthenticationToken(name, password, new ArrayList<>());
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
