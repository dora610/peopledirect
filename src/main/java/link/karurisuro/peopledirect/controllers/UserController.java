package link.karurisuro.peopledirect.controllers;

import link.karurisuro.peopledirect.entities.User;
import link.karurisuro.peopledirect.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jmx.export.annotation.ManagedOperation;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/")
    public String dashboard(Model model, Principal principal){
        includeUserDetails(model, principal);
        model.addAttribute("title", "User Dashboard");
        return "normal/user_dashboard";
    }

    @GetMapping("/add-contact")
    public String addContact(Model model, Principal principal) {
        model.addAttribute("title", "Add Contact");
        return "normal/add_contact_form";
    }

    @ModelAttribute
    private void includeUserDetails(Model model, Principal principal) {
        String userName = principal.getName();
        User user = null;
        try{
            user = userService.getUserByUserName(userName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("userName", userName);
        model.addAttribute("user", user);
    }
}