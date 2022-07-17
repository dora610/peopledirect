package link.karurisuro.peopledirect.controllers;

import link.karurisuro.peopledirect.entities.Contact;
import link.karurisuro.peopledirect.entities.User;
import link.karurisuro.peopledirect.helper.Message;
import link.karurisuro.peopledirect.service.ContactService;
import link.karurisuro.peopledirect.service.UserService;
import link.karurisuro.peopledirect.utils.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Objects;

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
    public String insertContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result, Model model, HttpSession session) {
        model.addAttribute("title", "Add Contact");
        if (result.hasErrors()) {
            log.error(result.toString());
            return "normal/add_contact_form";
        }
        try {
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
//        User user = includeUserDetails(model, principal);
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