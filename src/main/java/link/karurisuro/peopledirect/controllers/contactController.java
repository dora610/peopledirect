package link.karurisuro.peopledirect.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class contactController {
    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "People Direct | Home");
        model.addAttribute("app", "Welcome to PeopleDirect");
        model.addAttribute("page", "Home");

        return "index";
    }

    @GetMapping("/about")
    public String about(Model model) {
        model.addAttribute("title", "People Direct | About");
        model.addAttribute("app", "Welcome to PeopleDirect");
        model.addAttribute("page", "About");

        return "about";
    }
}
