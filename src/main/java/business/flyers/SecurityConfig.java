package business.flyers;

import business.flyers.Constants.Constants;
import business.flyers.Entities.UserModel;
import business.flyers.Repositories.UserModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

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
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests()
                    .antMatchers("/registration/**").permitAll()
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .loginPage("/login")
                    .permitAll();

            http.csrf().disable();
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
        admin.setSignUpDate(LocalDateTime.now());
        admin.setActivationKey("111111");
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
        user.setFirstName("dbadmin");
        user.setLastName("dbadmin");
        user.setUsername("dbadmin");
        user.setEmail("dbadmin");
        user.setPassword(passwordEncoder.encode("dbadmin"));
        user.setUserGroup(Constants.User.Role.DATABASEADMIN);
        userModelRepository.save(admin);
        userModelRepository.save(moderator);
        userModelRepository.save(user);
    }
}

