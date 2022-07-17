package link.karurisuro.peopledirect.service;

import link.karurisuro.peopledirect.entities.User;
import link.karurisuro.peopledirect.utils.NotFoundException;

public interface UserService {

    public void saveUserDetails(User user);
    public User getSingleUserDetails(String email, String password) throws Exception;

    User getUserByUserName(String email) throws NotFoundException;
}
