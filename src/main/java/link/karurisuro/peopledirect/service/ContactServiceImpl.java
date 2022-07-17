package link.karurisuro.peopledirect.service;

import link.karurisuro.peopledirect.dao.ContactRepository;
import link.karurisuro.peopledirect.entities.Contact;
import link.karurisuro.peopledirect.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Override
    public void addContact(Contact contact, User user) {
        contact.setUser_id(user);
        contactRepository.save(contact);
    }

    public List<Contact> getAllContacts(User user) {
        return contactRepository.findByUser(user.getId());
    }
}
