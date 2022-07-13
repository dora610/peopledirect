package link.karurisuro.peopledirect.service;

import link.karurisuro.peopledirect.entities.Contact;
import link.karurisuro.peopledirect.entities.User;

import java.util.List;

public interface ContactService {
    public void addContact(Contact contact, User user);
    public List<Contact> getAllContacts(User user);
}
