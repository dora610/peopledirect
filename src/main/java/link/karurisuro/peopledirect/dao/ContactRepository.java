package link.karurisuro.peopledirect.dao;

import link.karurisuro.peopledirect.entities.Contact;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contact, Long> {
}
