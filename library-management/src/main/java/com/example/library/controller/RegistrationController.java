package com.example.library.controller;


import com.example.library.model.User;
import com.example.library.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        // Logika walidacji i zapisu użytkownika
        if (bindingResult.hasErrors()) {
            return "registration"; // Powrót do formularza rejestracji z błędami
        }

        // Zapis użytkownika za pomocą UserService
        userService.saveUser(user);

        return "redirect:/login"; // Przekierowanie na stronę logowania po udanej rejestracji
    }
}