package business.flyers.Repositories;

import business.flyers.Entities.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserModelRepository extends JpaRepository<UserModel, Long>{
    UserModel findOneByUsername(String username);
    UserModel findOneByActivationKey(String activationKey);
}
