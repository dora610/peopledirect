package link.karurisuro.peopledirect.controllers;

import link.karurisuro.peopledirect.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
public class contactController {
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
    public String addSignUp(@ModelAttribute User user) {
        log.debug("model attribute:\n {}", user);
        return "redirect:/signin";
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
