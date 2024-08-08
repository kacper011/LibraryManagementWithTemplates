package com.example.library.controller;

import com.example.library.dto.UserDto;
import com.example.library.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.stream.Collectors;

@Controller
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/books/accounts")
    public String listUserAccounts(Model model) {
        var users = userRepository.findAll();


        var userDtos = users.stream()
                .map(user -> new UserDto(user.getName(), user.getRoles().stream()
                        .map(role -> role.getName().replace("ROLE_", ""))
                        .collect(Collectors.toSet())))
                .collect(Collectors.toList());

        model.addAttribute("users", userDtos);
        return "accounts";
    }
}
