package business.flyers.Services;

import business.flyers.Entities.UserModel;
import business.flyers.Populators.UserModelPopulator;
import business.flyers.Repositories.UserModelRepository;
import business.flyers.dto.DefaultUserDetails;
import business.flyers.dto.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class DefaultUserDetailsService implements UserDetailsService {
    @Autowired
    private UserModelRepository userModelRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserModelPopulator userModelPopulator;

    @Value("${siteURL}")
    private String baseURL;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserModel user = userModelRepository.findOneByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(username);
        }
        return new DefaultUserDetails(user);
    }

    public List<DefaultUserDetails> getUsers(int from, int to) {
        return userModelRepository.findAll().subList(from, to > userModelRepository.count() ? (int) userModelRepository.count() : to)
                .stream().map(x -> new DefaultUserDetails(x)).collect(Collectors.toList());
    }

    public int countUsers(){
        return (int) userModelRepository.count();
    }

    public void createUserFromRegistrationForm(RegistrationForm registrationForm) {
        UserModel userModel = new UserModel();
        userModelPopulator.populateFromRegistrationForm(registrationForm, userModel, new BCryptPasswordEncoder());
        String uuid = UUID.randomUUID().toString();
        userModel.setActivationKey(uuid);
        userModel.setSignUpDate(LocalDateTime.now());
        userModel.setUserGroup("user");
        emailService.sendEmail(userModel.getEmail(), "Account activation",
                "Please activate your account by navigating to the following link " + baseURL + "/registration/activation?key=" + uuid);
        userModelRepository.save(userModel);
    }

    public UserModel loadUserByActivationKey(HttpServletRequest request, String key) {
        UserModel user = userModelRepository.findOneByActivationKey(key);
        if(user != null) {
            user.validateActivationKey(key);
            userModelRepository.save(user);
            authenticateUserAndSetSession(request, user);
            return user;
        } else {
            throw new UsernameNotFoundException("User with provided key not found" + key);
        }
    }

    private void authenticateUserAndSetSession(HttpServletRequest request, UserModel user) {
        UserDetails userDetails = new DefaultUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
