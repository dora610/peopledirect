package link.karurisuro.peopledirect.dao;

import link.karurisuro.peopledirect.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query("select c from Contact c where c.user.id=:userId")
    List<Contact> findByUser(@Param("userId") Long userId);

}
