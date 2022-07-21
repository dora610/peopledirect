package link.karurisuro.peopledirect.service;

import link.karurisuro.peopledirect.dao.ContactRepository;
import link.karurisuro.peopledirect.dao.UserRepository;
import link.karurisuro.peopledirect.entities.Contact;
import link.karurisuro.peopledirect.entities.User;
import link.karurisuro.peopledirect.utils.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Service
public class ContactServiceImpl implements ContactService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void addContact(Contact contact, User user, MultipartFile file) throws Exception {
        uploadImage(file, contact);
        contact.setUser(user);
        contactRepository.save(contact);
    }

    @Override
    public Page<Contact> getAllContacts(User user, int page, int limit) {
        Pageable firstPage = PageRequest.of(page, limit);
        return contactRepository.findByUser(user.getId(), firstPage);
    }

    @Override
    public Contact getSingleContact(Long id) throws NotFoundException {
        return contactRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("No such contact exists :("));
    }


    public Contact getSingleContact(Long id, String userName) throws NotFoundException {
        User user = userRepository.findByEmail(userName).orElseThrow(() -> new NotFoundException("You are not allowed to view that!!"));

        Contact contact = contactRepository.findByIdAndUser(id, user.getId())
                .orElseThrow(() -> new NotFoundException("No such contact exists :("));
        contact.setUser(null);
        return contact;
    }

    @Override
    public void updateContact(Contact contact, User user, Long contactId, MultipartFile file) throws Exception {
        Contact savedContact = contactRepository.findById(contactId).orElseThrow(() -> new NotFoundException("No such contact found :("));
        contactMapper(savedContact, contact);
        savedContact.setUser(user);
        uploadImage(file, savedContact);
        contactRepository.saveAndFlush(savedContact);
        log.debug("contact updated");
    }

    private void uploadImage(MultipartFile file, Contact contact) throws Exception {
        if (file.isEmpty()) {
            contact.setImgUrl("default_profile.png");
        } else if (!(file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg"))) {
//            session.setAttribute("message", new Message("Only jpeg. png file will be accepted", "danger"));
//            return "normal/add_contact_form";
            throw new Exception("Only jpeg. png file will be accepted");
        } else {
            log.debug("file input name: {}", file.getName());
            log.debug("file name: {}", file.getOriginalFilename());
            log.debug("file size: {}", file.getSize());
            log.debug("file content type: {}", file.getContentType());

            String ext = file.getContentType().split("/")[1];
            // uuid generation
            String fileName = UUID.randomUUID().toString() + "." + ext;

            // implementation returns a File reference for the given URI-identified resource,
            // provided that it refers to a file in the file system.
            File fileToBeSaved = new ClassPathResource("static/uploads").getFile();
            Path uploadPath = Paths.get(fileToBeSaved.getAbsolutePath(), File.separator, fileName);
            Files.copy(file.getInputStream(), uploadPath, StandardCopyOption.REPLACE_EXISTING);
            log.debug("Image uploaded at : {}", uploadPath.toString());

            contact.setImgUrl(fileName);
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
    }


}
