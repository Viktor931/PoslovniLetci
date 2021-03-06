package business.flyers.Entities;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

import static business.flyers.Constants.Constants.Login.MAX_FAILED_ATTEMPTS;

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
    private boolean twoStepLogin;
    private int failedAttempts;

    @Autowired
    private static PasswordEncoder passwordEncoder;
    private String loginKey;
    private LocalDateTime loginTime;

	public void failedLogin() {
	    failedAttempts++;
	}

	public void successfulLogin() {
		failedAttempts = 0;
	}

	public Long getId() {
        return id;
    }

	public boolean isLocked() {
		return failedAttempts >= MAX_FAILED_ATTEMPTS;
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

    public boolean isActivated(){
        return StringUtils.isEmpty(activationKey);
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

    public void setTwoStepLogin(boolean twoStepLogin) {
        this.twoStepLogin = twoStepLogin;
    }

    public boolean getTwoStepLogin() {
        return twoStepLogin;
    }

    public void setLoginKey(String loginKey) {
        this.loginKey = loginKey;
    }

    public String getLoginKey() {
        return loginKey;
    }

    public LocalDateTime getSignUpDate() {
        return signUpDate;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }
}
