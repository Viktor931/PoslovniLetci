package business.flyers.Security;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import business.flyers.Constants.Constants;
import business.flyers.Entities.UserModel;
import business.flyers.Repositories.UserModelRepository;
import business.flyers.Security.CustomAuthenticationProvider;

@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserModelRepository userModelRepository;

    @Configuration
    @Order(1)
    public static class WebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        protected void configure(HttpSecurity http) throws Exception {
            http.antMatcher("/privatno").authorizeRequests().anyRequest().hasRole(Constants.User.Role.DATABASEADMIN).and().httpBasic();
        }
    }

    @Configuration
    public static class RegularWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
		@Autowired
		private CustomAuthenticationProvider customAuthenticationProvider;

		@Override
		protected void configure(AuthenticationManagerBuilder auth) throws Exception {

			auth.authenticationProvider(customAuthenticationProvider);
		}
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/registration/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .successHandler(new CookieAuthenticationSuccessHandler())
                    .permitAll();

            http.csrf().disable();
            http.requiresChannel().anyRequest().requiresSecure();
        }

        class CookieAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
            private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

            @Override
            public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                Cookie cookie = new Cookie("user", authentication.getPrincipal().toString());
                httpServletResponse.addCookie(cookie);
                redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, "/home");
            }
        }
    }

    @PostConstruct
    public void initUsers() {
        UserModel admin = new UserModel();
        admin.setFirstName("admin");
        admin.setLastName("admin");
        admin.setUsername("admin");
        admin.setEmail("admin");
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setUserGroup(Constants.User.Role.ADMIN);
        UserModel moderator = new UserModel();
        moderator.setFirstName("moderator");
        moderator.setLastName("moderator");
        moderator.setUsername("moderator");
        moderator.setEmail("moderator");
        moderator.setPassword(passwordEncoder.encode("moderator"));
        moderator.setUserGroup(Constants.User.Role.MODERATOR);
        UserModel user = new UserModel();
        user.setFirstName("user");
        user.setLastName("user");
        user.setUsername("user");
        user.setEmail("user");
        user.setPassword(passwordEncoder.encode("user"));
        user.setUserGroup(Constants.User.Role.USER);
        UserModel dbadmin = new UserModel();
        dbadmin.setFirstName("dbadmin");
        dbadmin.setLastName("dbadmin");
        dbadmin.setUsername("dbadmin");
        dbadmin.setEmail("dbadmin");
        dbadmin.setPassword(passwordEncoder.encode("dbadmin"));
        dbadmin.setUserGroup(Constants.User.Role.DATABASEADMIN);
        userModelRepository.save(admin);
        userModelRepository.save(moderator);
        userModelRepository.save(user);
        userModelRepository.save(dbadmin);
    }
}

