package link.karurisuro.peopledirect.service;

import link.karurisuro.peopledirect.entities.Contact;
import link.karurisuro.peopledirect.entities.User;
import link.karurisuro.peopledirect.utils.NotFoundException;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface ContactService {
    public void addContact(Contact contact, User user, MultipartFile file) throws Exception;
    public Page<Contact> getAllContacts(User user, int page, int limit);

    public Contact getSingleContact(Long id) throws NotFoundException;
    public Contact getSingleContact(Long id, String name) throws NotFoundException;
    public void updateContact(Contact contact, User user, Long contactId, MultipartFile file) throws Exception;
    public void deleteContact(Long contactId, String userName) throws NotFoundException, IOException;
    public List<Contact> searchContact(String searchStr) throws Exception;
}
