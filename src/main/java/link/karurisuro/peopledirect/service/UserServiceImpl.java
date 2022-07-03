package link.karurisuro.peopledirect.service;

import link.karurisuro.peopledirect.dao.UserRepository;
import link.karurisuro.peopledirect.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public void saveUserDetails(User user) {
        user.setRole("ROLE_USER");
        user.setEnabled(true);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        log.debug("saved user: {}", savedUser);
    }

    @Override
    public User getSingleUserDetails(String email, String password) throws Exception{
        User user = userRepository.findByEmail(email).orElseThrow(() -> new Exception("UserName not found!!"));
        if(passwordEncoder.matches(password, user.getPassword())){
            return user;
        }else {
            throw new Exception("Incorrect password!!");
        }
    }
}
