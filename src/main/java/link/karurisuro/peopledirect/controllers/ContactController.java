package link.karurisuro.peopledirect.controllers;

import link.karurisuro.peopledirect.entities.User;
import link.karurisuro.peopledirect.helper.Message;
import link.karurisuro.peopledirect.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@Slf4j
public class ContactController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home(Model model, Principal principal) {
        model.addAttribute("title", "People Direct | Home");
        model.addAttribute("app", "Welcome to PeopleDirect");
        Optional.ofNullable(principal).ifPresent(p -> model.addAttribute("userName", p.getName()));
        return "index";
    }

    @GetMapping("/about")
    public String about(Model model, Principal principal) {
        model.addAttribute("title", " About");
        Optional.ofNullable(principal).ifPresent(p -> model.addAttribute("userName", p.getName()));
        return "about";
    }

    @GetMapping("/signup")
    public String loadSignUp(Model model) {
        model.addAttribute("title", "Sign Up");
        model.addAttribute("user", new User());
        return "signup";
    }

    @PostMapping("/signup")
    public String addSignUp(@Valid @ModelAttribute("user") User user,
                            BindingResult result,
                            @RequestParam(value = "agreement", defaultValue = "false") boolean isAgree,
                            Model model,
                            HttpSession session) {

        log.debug("model attribute:\n {}", user);
        log.debug("agree: {}", isAgree);

        if (result.hasErrors()) {
            log.error(result.toString());
            model.addAttribute("user", user);
            return "signup";
        }

        try {
            if (!isAgree) {
                throw new Exception("You are not agreed with our terms & conditions!!");
            }
            userService.saveUserDetails(user);
            log.debug("user saved");
            model.addAttribute("user", new User());
            session.setAttribute("message", new Message("Successfully signed up", "success"));
            return "redirect:/";
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            model.addAttribute("user", user);
            String m = e.getMessage().isBlank() ? "Something went wrong!!" : e.getMessage();
            session.setAttribute("message", new Message(m, "danger"));
            return "redirect:/signup";
        }

    }

    @GetMapping("/signin")
    public String loadSignIn(Model model) {
        model.addAttribute("title", "Sign In");
        model.addAttribute("user", new User());
        return "signin";
    }

    /*@PostMapping("/signin")
    public String addSignIn(@ModelAttribute User user, HttpSession httpSession) {
        try {
            User savedUser = userService.getSingleUserDetails(user.getEmail(), user.getPassword());
            log.debug("User found: {}", savedUser);
            httpSession.setAttribute("message", new Message("Welcome "+ savedUser.getEmail(), "success"));
            return "redirect:/";
        } catch (Exception e) {
            httpSession.setAttribute("message", new Message("Error: " + e.getMessage(),  "danger"));
            return "redirect:/signin";
        }
    }*/
}
