package business.flyers.Repositories;

import business.flyers.Entities.UserModel;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserModelRepositoryTest {
    @Autowired
    private UserModelRepository userModelRepository;
    @Test
    public void testGetUser(){
        UserModel userModel = new UserModel();
        userModel.setUsername("aa");
        userModel.setEmail("qq");
        userModelRepository.save(userModel);
        Assert.assertEquals(((UserModel)userModelRepository.findOneByUsername("aa")).getEmail(), "qq");
    }
}
