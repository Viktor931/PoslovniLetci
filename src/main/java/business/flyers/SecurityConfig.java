package business.flyers;

import business.flyers.Entities.UserModel;
import business.flyers.Repositories.UserModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.PostConstruct;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserModelRepository userModelRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/registration", "/registration/nameCheck").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll();

        http.csrf().disable();
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
        admin.setPassword(passwordEncoder.encode("admin"));
        admin.setUserGroup("admin"); //todo
        UserModel moderator = new UserModel();
        moderator.setFirstName("moderator");
        moderator.setLastName("moderator");
        moderator.setUsername("moderator");
        moderator.setPassword(passwordEncoder.encode("moderator"));
        moderator.setUserGroup("moderator"); //todo
        UserModel user = new UserModel();
        user.setFirstName("user");
        user.setLastName("user");
        user.setUsername("user");
        user.setPassword(passwordEncoder.encode("user"));
        user.setUserGroup("user"); //todo
        userModelRepository.save(admin);
        userModelRepository.save(moderator);
        userModelRepository.save(user);
    }
}

