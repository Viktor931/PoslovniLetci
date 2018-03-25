package business.flyers.dto;

import business.flyers.Constants.Constants;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotEmpty;

public class RegistrationForm {
    private String name;
    private String lastName;
    private String username;
    private String email;
    private String password;
    private String password2;
    private boolean twoStepLogin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }

    public void setTwoStepLogin(boolean twoStepLogin) {
        this.twoStepLogin = twoStepLogin;
    }

    public boolean getTwoStepLogin() {
        return twoStepLogin;
    }
}
