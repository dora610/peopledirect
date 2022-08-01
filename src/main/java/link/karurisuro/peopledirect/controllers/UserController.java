package link.karurisuro.peopledirect.controllers;

import link.karurisuro.peopledirect.dto.ContactDto;
import link.karurisuro.peopledirect.entities.Contact;
import link.karurisuro.peopledirect.entities.User;
import link.karurisuro.peopledirect.helper.Message;
import link.karurisuro.peopledirect.service.ContactService;
import link.karurisuro.peopledirect.service.UserService;
import link.karurisuro.peopledirect.utils.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

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
        model.addAttribute("action", "/user/add-contact");
        model.addAttribute("method", "POST");
        model.addAttribute("btn_label", "Add Contact");
        return "normal/add_contact_form";
    }

    @PostMapping("/add-contact")
    public String insertContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result,
            @RequestParam("profileImage") MultipartFile file, Model model, HttpSession session) {
        model.addAttribute("title", "Add Contact");
        model.addAttribute("action", "/user/add-contact");
        model.addAttribute("method", "POST");
        model.addAttribute("btn_label", "Add Contact");
        if (result.hasErrors()) {
            log.error(result.toString());
            return "normal/add_contact_form";
        }

        try {
            contactService.addContact(contact, (User) model.getAttribute("user"), file);
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
    public String viewContacts(Model model, HttpSession session, @ModelAttribute("user") User user,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "limit", defaultValue = "6") int limit) {
        model.addAttribute("title", "View Contacts");
        try {
            Page<Contact> pageableContacts = contactService.getAllContacts(user, page, limit);

            log.debug("total no. of elements: {}", pageableContacts.getTotalElements());
            log.debug("total no. of pages: {}", pageableContacts.getTotalPages());
            log.debug("current page: {}", pageableContacts.getNumber());

            List<Contact> contactList = pageableContacts.toList();

            List<Integer> pageList = new ArrayList<>();
            for (int i = 0; i < pageableContacts.getTotalPages(); i++) {
                pageList.add(i + 1);
            }

            model.addAttribute("contacts", contactList);
            model.addAttribute("totalEle", pageableContacts.getTotalElements());
            model.addAttribute("totalPages", pageableContacts.getTotalPages());
            model.addAttribute("pages", pageList);
            model.addAttribute("currPage", pageableContacts.getNumber());
        } catch (Exception e) {
            e.printStackTrace();
            String messageString = e.getMessage().isBlank() ? "Something went wrong :(" : e.getMessage();
            session.setAttribute("message", new Message(messageString, "warning"));
            return "normal/user_dashboard";
        }
        return "normal/view_contacts";
    }

    @GetMapping("/update-contact")
    public String showUpdateContact(Model model, @ModelAttribute("contact") Contact contact,
            @RequestParam(name = "id") Long id,
            HttpSession session) {
        model.addAttribute("title", "Update Contact");
        model.addAttribute("action", "/user/update-contact?id=" + id);
        model.addAttribute("method", "PUT");
        model.addAttribute("btn_label", "Update Contact");
        try {
            Contact contactDetails = contactService.getSingleContact(id);
            model.addAttribute("contact", contactDetails);
        } catch (NotFoundException e) {
            e.printStackTrace();
            session.setAttribute("message", new Message(e.getMessage(), "warning"));
            return "normal/view-contacts";
        }
        return "normal/add_contact_form";
    }

    @PutMapping("/update-contact")
    public String updateContact(@Valid @ModelAttribute("contact") Contact contact, BindingResult result,
            @RequestParam("profileImage") MultipartFile file, Model model, HttpSession session,
            @RequestParam(name = "id") Long id) {
        model.addAttribute("title", "Update Contact");
        model.addAttribute("action", "/user/update-contact?id=" + id);
        model.addAttribute("method", "PUT");
        model.addAttribute("btn_label", "Update Contact");
        if (result.hasErrors()) {
            log.error(result.toString());
            return "normal/add_contact_form";
        }

        try {
            contactService.updateContact(contact, (User) model.getAttribute("user"), id, file);
            session.setAttribute("message", new Message("Contact details saved successfully", "success"));
        } catch (Exception e) {
            e.printStackTrace();
            String messageString = e.getMessage().isBlank() ? "Something went wrong :(" : e.getMessage();
            session.setAttribute("message", new Message(messageString, "danger"));
            return "normal/add_contact_form";
        }
        return "redirect:/user/view-contacts";
    }

    @ResponseBody
    @GetMapping("/view-contact/{id}")
    public ResponseEntity<Contact> viewSingleContact(@PathVariable(name = "id") Long id, Principal principal) {
        try {
            Contact contact = contactService.getSingleContact(id, principal.getName());
            return ResponseEntity.ok().body(contact);
        } catch (NotFoundException e) {
            e.printStackTrace();
            return ResponseEntity.notFound().build();
        }
    }

    @ResponseBody
    @DeleteMapping("/delete-contact/{id}")
    public ResponseEntity<Message> deleteContact(@PathVariable(name = "id") Long id, Principal principal,
            HttpSession session) {
        try {
            session.setAttribute("message", new Message("Contact details deleted successfully", "success"));
            contactService.deleteContact(id, principal.getName());
            log.debug("contact deleted - {}", id);
            return ResponseEntity.ok().body(new Message("Contact details deleted successfully", "success"));
        } catch (NotFoundException ne) {
            ne.printStackTrace();
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new Message(e.getMessage(), "danger"));
        }
    }

    @GetMapping("/profile")
    public String showProfile(Principal principal, Model model, HttpSession session) {
        try {
            User user = userService.getUserByUserName(principal.getName());
            model.addAttribute("user", user);
        } catch (NotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            session.setAttribute("message", new Message("Unauthorized acceess!!", "danger"));
            return "redirect:/user/";
        }

        return "normal/user_profile";
    }

    @ResponseBody
    @GetMapping("/api/contact")
    public ResponseEntity<Map<Long, String>> searchContact(@RequestParam(name = "q") String searchStr,
            Principal principal) {
        log.debug("search string: {}", searchStr);
        Map<Long, String> contactMap = new HashMap<>();
        try {
            contactService.searchContact(searchStr).forEach(c -> contactMap.put(c.getId(), c.getName()));        
            return ResponseEntity.ok().body(contactMap);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(contactMap);
        }
    }

}