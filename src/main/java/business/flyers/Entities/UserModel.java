package business.flyers.Entities;

import business.flyers.Constants.Constants;
import business.flyers.dto.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String firstName;
    private String lastName;
    @Column(nullable = false, unique = true)
    private String username;
    private String email;
    private String password;
    private String rawPassword;
    private String userGroup;
    private String activationKey;
    private LocalDateTime signUpDate;

    @Autowired
    private static PasswordEncoder passwordEncoder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public void setUserGroup(String userGroupModel) {
        this.userGroup = userGroupModel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setActivationKey(String activationKey) {
        this.activationKey = activationKey;
    }

    public boolean isValid(){
        return StringUtils.isEmpty(activationKey);
    }

    public boolean validateActivationKey(String activationKey){
        if(!StringUtils.isEmpty(this.activationKey) && this.activationKey.equals(activationKey) && isActivationKeyActive()){
            this.activationKey = "";
            return true;
        }
        return false;
    }

    private boolean isActivationKeyActive() {
        return LocalDateTime.now().isBefore(signUpDate.plusHours(Constants.Registration.Activation.TIMEOUT_IN_HOURS));
    }

    public void setSignUpDate(LocalDateTime signUpDate) {
        this.signUpDate = signUpDate;
    }

    public String getRawPassword() {
        return rawPassword;
    }

    public void setRawPassword(String rawPassword) {
        this.rawPassword = rawPassword;
    }
}
