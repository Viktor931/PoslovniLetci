package business.flyers.Services;

import business.flyers.Constants.Constants;
import business.flyers.Entities.UserModel;
import business.flyers.Populators.UserModelPopulator;
import business.flyers.Repositories.UserModelRepository;
import business.flyers.dto.DefaultUserDetails;
import business.flyers.dto.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.apache.commons.lang3.RandomStringUtils;

@Service
public class DefaultUserDetailsService implements UserDetailsService {
    @Autowired
    private UserModelRepository userModelRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserModelPopulator userModelPopulator;
    @Autowired
	private PasswordEncoder passwordEncoder;

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
                "Please activate your account by navigating to the following link " + baseURL + "registration/activation?key=" + uuid);
        userModelRepository.save(userModel);
    }

    public UserModel loadUserByActivationKey(HttpServletRequest request, String key) {
        UserModel user = userModelRepository.findOneByActivationKey(key);
        if(user != null && !StringUtils.isEmpty(key) && activationKeyActive(user)) {
            user.setActivationKey("");
            userModelRepository.save(user);
            authenticateUserAndSetSession(request, user);
            return user;
        } else {
            throw new UsernameNotFoundException("User with provided activation key not found " + key);
        }
    }

    private boolean activationKeyActive(UserModel userModel) {
        return LocalDateTime.now().isBefore(userModel.getSignUpDate().plusHours(Constants.Registration.Activation.TIMEOUT_IN_HOURS));
    }

    private void authenticateUserAndSetSession(HttpServletRequest request, UserModel user) {
        UserDetails userDetails = new DefaultUserDetails(user);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(),
                                                                                userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    public UserModel loadUserByLoginKey(HttpServletRequest request, String key) {
        UserModel user = userModelRepository.findOneByLoginKey(key);
        if(user != null && !StringUtils.isEmpty(key) && loginKeyActive(user)) {
            user.setLoginKey("");
            userModelRepository.save(user);
            authenticateUserAndSetSession(request, user);
            return user;
        } else {
            throw new UsernameNotFoundException("User with provided login key not found " + key);
        }
    }

    private boolean loginKeyActive(UserModel userModel) {
        return LocalDateTime.now().isBefore(userModel.getLoginTime().plusMinutes(Constants.Login.TIMEOUT_IN_MINUTES));
    }

    public void createLoginKey(UserModel userModel) {
        String uuid = UUID.randomUUID().toString();
        userModel.setLoginKey(uuid);
        userModel.setLoginTime(LocalDateTime.now());
        userModelRepository.save(userModel);
        emailService.sendEmail(userModel.getEmail(), "Login key", "To login navigate to the following link " + baseURL + "login/confirm?key=" + uuid);
    }

    public void saveUser(final UserModel userModel) {
        userModelRepository.save(userModel);
    }

    public void resetPassword(String username) {
        UserModel userModel = userModelRepository.findOneByUsername(username);
        String password = RandomStringUtils.random(10, true, true);
        userModel.setPassword(passwordEncoder.encode(password));
        userModelRepository.save(userModel);
		emailService.sendEmail(userModel.getEmail(), "Password reset", "Your new password is " + password);
    }
}
