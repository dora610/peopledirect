package link.karurisuro.peopledirect.dao;

import link.karurisuro.peopledirect.entities.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByEmail(String email);
    public Optional<User> findByEmailAndPassword(String email, String password);
}
