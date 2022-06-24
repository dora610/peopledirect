package link.karurisuro.peopledirect.controllers;

import link.karurisuro.peopledirect.entities.User;
import link.karurisuro.peopledirect.helper.Message;
import link.karurisuro.peopledirect.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class contactController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "People Direct | Home");
        model.addAttribute("app", "Welcome to PeopleDirect");
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", " About");
        return "about";
    }

    @GetMapping("/signup")
    public String loadSignUp(Model model) {
        model.addAttribute("title", "Sign Up");
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String addSignUp(
            @ModelAttribute("user") User user,
            @RequestParam(value = "agreement", defaultValue = "false") boolean isAgree,
            Model model,
            HttpSession session) {
        log.debug("model attribute:\n {}", user);
        log.debug("agree: {}", isAgree);

        try {
            if (!isAgree) {
                log.error("You are not agreed with our terms & conditions!!");
                throw new Exception("You are not agreed with our terms & conditions!!");
            }
            userService.saveUserDetails(user);
            log.debug("user saved");
            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully signed up", "alert-success"));
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong!!", "alert-danger"));
            return "redirect:/signup";
        }

    }

    @GetMapping("/signin")
    public String loadSignIn(Model model) {
        model.addAttribute("title", "Sign Up");
        model.addAttribute("user", new User());
        return "signin";
    }

    @PostMapping("/signin")
    public String addSignIn(@ModelAttribute User user) {
        log.debug("model attribute:\n {}", user);
        return "redirect:/signin";
    }
}
