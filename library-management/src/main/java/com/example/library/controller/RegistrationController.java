package com.example.library.controller;

import com.example.library.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
//        model.addAttribute("user", new User());
        return "redirect:/login";
    }
//
//    @PostMapping("/registration")
//    public String processRegistration(User user, BindingResult result) {
//
//        if (result.hasErrors()) {
//            return "registration";
//        }
//        return "redirect:/login";
//    }
}
