package link.karurisuro.peopledirect.dao;

import link.karurisuro.peopledirect.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
