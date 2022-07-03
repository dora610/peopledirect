package link.karurisuro.peopledirect.service;

import link.karurisuro.peopledirect.dao.UserRepository;
import link.karurisuro.peopledirect.entities.User;
import link.karurisuro.peopledirect.entities.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("No such user found"));

        return new UserDetailsImpl(user.getEmail(), user.getPassword(), user.isEnabled(), user.getRole());
    }
}
