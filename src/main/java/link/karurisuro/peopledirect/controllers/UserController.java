package link.karurisuro.peopledirect.controllers;

import link.karurisuro.peopledirect.entities.Contact;
import link.karurisuro.peopledirect.entities.User;
import link.karurisuro.peopledirect.helper.Message;
import link.karurisuro.peopledirect.service.ContactService;
import link.karurisuro.peopledirect.service.UserService;
import link.karurisuro.peopledirect.utils.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;

    @ModelAttribute
    private void includeUserDetails(Model model, Principal principal) {
        String userName = principal.getName();
        User user = null;
        try {
            user = userService.getUserByUserName(userName);
            log.debug("logged in user: {}", user.getEmail());
        } catch (NotFoundException e) {
            e.printStackTrace();
            model.addAttribute("message", "Not logged in");
        }
        model.addAttribute("userName", userName);
        model.addAttribute("user", user);
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("title", "User Dashboard");
        return "normal/user_dashboard";
    }

    @GetMapping("/add-contact")
    public String addContact(Model model, @ModelAttribute("contact") Contact contact) {
        model.addAttribute("title", "Add Contact");
        return "normal/add_contact_form";
    }

    @PostMapping("/add-contact")
    public String insertContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result,
                                @RequestParam("profileImage") MultipartFile file, Model model, HttpSession session) {
        model.addAttribute("title", "Add Contact");
        if (result.hasErrors()) {
            log.error(result.toString());
            return "normal/add_contact_form";
        }

        try {
            if(file.isEmpty()){
                contact.setImgUrl("default_profile.png");
            }
            else if(!(file.getContentType().equals("image/png") || file.getContentType().equals("image/jpeg"))){
                session.setAttribute("message", new Message("Only jpeg. png file will be accepted", "danger"));
                return "normal/add_contact_form";
            }
            else {
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
            contactService.addContact(contact, (User) model.getAttribute("user"));
            session.setAttribute("message", new Message("Contact details saved successfully", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            String messageString = e.getMessage().isBlank() ? "Something went wrong :(" : e.getMessage();
            session.setAttribute("message", new Message(messageString, "danger"));
            return "normal/add_contact_form";
        }
        return "redirect:/user/view-contacts";
    }

    @GetMapping("/view-contacts")
    public String viewContacts(Model model, HttpSession session, @ModelAttribute("user") User user) {
        model.addAttribute("title", "View Contacts");
        try {
            List<Contact> contacts = contactService.getAllContacts(user);
            model.addAttribute("contacts", contacts);
        } catch (Exception e) {
            e.printStackTrace();
            String messageString = e.getMessage().isBlank() ? "Something went wrong :(" : e.getMessage();
            session.setAttribute("message", new Message(messageString, "warning"));
            return "normal/user_dashboard";
        }
        return "normal/view_contacts";
    }
}