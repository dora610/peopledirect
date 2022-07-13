package link.karurisuro.peopledirect.controllers;

import link.karurisuro.peopledirect.entities.Contact;
import link.karurisuro.peopledirect.entities.User;
import link.karurisuro.peopledirect.helper.Message;
import link.karurisuro.peopledirect.service.ContactService;
import link.karurisuro.peopledirect.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ContactService contactService;


    @GetMapping("/")
    public String dashboard(Model model, Principal principal) {
        includeUserDetails(model, principal);
        model.addAttribute("title", "User Dashboard");
        return "normal/user_dashboard";
    }

    @GetMapping("/add-contact")
    public String addContact(Model model, Principal principal) {
        includeUserDetails(model, principal);
        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";
    }

    @PostMapping("/add-contact")
    public String insertContact(@ModelAttribute("contact") Contact contact, Model model, Principal principal, HttpSession session) {
//        includeUserDetails(model, principal);
        model.addAttribute("title", "Add Contact");
        try {
            User user = userService.getUserByUserName(principal.getName());
            contactService.addContact(contact, user);
            model.addAttribute("contact", new Contact());
            session.setAttribute("message", new Message("Contact details saved successfully", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            String messageString = e.getMessage().isBlank() ? "Something went wrong :(" : e.getMessage();
            model.addAttribute("contact", contact);
            session.setAttribute("message", new Message(messageString, "danger"));
            return "normal/add_contact_form";
        }
        return "redirect:/user/";
    }

    @GetMapping("/view-contacts")
    public String viewContacts(Model model, Principal principal, HttpSession session) {
        User user = includeUserDetails(model, principal);
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

    @ModelAttribute
    private User includeUserDetails(Model model, Principal principal) {
        String userName = principal.getName();
        User user = null;
        try {
            user = userService.getUserByUserName(userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("userName", userName);
        model.addAttribute("user", user);
        return user;
    }
}