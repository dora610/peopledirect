package link.karurisuro.peopledirect.service;

import link.karurisuro.peopledirect.entities.Contact;
import link.karurisuro.peopledirect.entities.User;
import link.karurisuro.peopledirect.utils.NotFoundException;

import java.util.List;

public interface ContactService {
    public void addContact(Contact contact, User user);
    public List<Contact> getAllContacts(User user);

    public Contact getSingleContact(Long id) throws NotFoundException;
    public void updateContact(Contact contact, User user, Long contactId) throws NotFoundException;
}
