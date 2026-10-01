package com.devon.building.controller;

import com.devon.building.form.CustomerForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/403")
    public String accessDenied() {
        return "/403";
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/contact")
    public String contact() {
        return "contact";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("customer", new CustomerForm());
        return "register";
    }
}
