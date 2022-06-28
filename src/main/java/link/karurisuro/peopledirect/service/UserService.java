package link.karurisuro.peopledirect.service;

import link.karurisuro.peopledirect.entities.User;

public interface UserService {

    public void saveUserDetails(User user);
    public User getSingleUserDetails(String email, String password) throws Exception;
}
