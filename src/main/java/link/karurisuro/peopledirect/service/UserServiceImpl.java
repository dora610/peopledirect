package link.karurisuro.peopledirect.service;

import link.karurisuro.peopledirect.dao.UserRepository;
import link.karurisuro.peopledirect.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    UserRepository userRepository;

    @Override
    public void saveUserDetails(User user) {
        user.setRole("USER_ROLE");
        user.setEnabled(true);
        User savedUser = userRepository.save(user);
        log.debug("saved user: {}", savedUser);
    }
}
