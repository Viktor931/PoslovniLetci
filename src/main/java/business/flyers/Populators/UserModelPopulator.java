package business.flyers.Populators;

import business.flyers.Entities.UserModel;
import business.flyers.dto.RegistrationForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserModelPopulator {

    public void populateFromRegistrationForm(RegistrationForm registrationForm, UserModel userModel, PasswordEncoder passwordEncoder) {
        userModel.setFirstName(registrationForm.getName());
        userModel.setLastName(registrationForm.getLastName());
        userModel.setUsername(registrationForm.getUsername());
        userModel.setEmail(registrationForm.getEmail());
        userModel.setPassword(passwordEncoder.encode(registrationForm.getPassword()));
        userModel.setRawPassword(registrationForm.getPassword());
    }
}
