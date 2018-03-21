package business.flyers.Services;

import business.flyers.Entities.UserModel;
import business.flyers.Repositories.UserModelRepository;
import business.flyers.dto.DefaultUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class DefaultUserDetailsService implements UserDetailsService {

    @Autowired
    private UserModelRepository userModelRepository;

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
}
