package link.karurisuro.peopledirect.service;

import link.karurisuro.peopledirect.dao.ContactRepository;
import link.karurisuro.peopledirect.entities.Contact;
import link.karurisuro.peopledirect.entities.User;
import link.karurisuro.peopledirect.utils.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Override
    public void addContact(Contact contact, User user) {
        contact.setUser(user);
        contactRepository.save(contact);
    }

    @Override
    public List<Contact> getAllContacts(User user) {
        return contactRepository.findByUser(user.getId());
    }

    @Override
    public Contact getSingleContact(Long id) throws NotFoundException {
        return contactRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("No such contact exists :("));
    }

    @Override
    public void updateContact(Contact contact, User user, Long contactId) throws NotFoundException {
        contact.setUser(user);
        if (contactRepository.existsById(contactId)) {
            Contact savedContact = contactRepository.getReferenceById(contactId);
            contactMapper(savedContact, contact);
            contactRepository.saveAndFlush(savedContact);
            log.debug("contact updated");
        }else {
            throw new NotFoundException("No such contact found :(");
        }
    }

    private void contactMapper(Contact savedContact, Contact contact) {
        savedContact.setName(contact.getName());
        savedContact.setUser(contact.getUser());
        savedContact.setDesignation(contact.getDesignation());
        savedContact.setEmail(contact.getEmail());
        savedContact.setImgUrl(contact.getImgUrl());
        savedContact.setDescription(contact.getDescription());
        savedContact.setPhone(contact.getPhone());
        savedContact.setUser(contact.getUser());
    }


}
