package com.example.library.controller;

import com.example.library.dto.UserDto;
import com.example.library.repository.UserRepository;
import com.example.library.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.stream.Collectors;

@Controller
public class AdminController {

    private final UserRepository userRepository;
    private final UserService userService;

    public AdminController(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @GetMapping("/books/accounts")
    public String listUserAccounts(Model model) {
        var users = userRepository.findAll();


        var userDtos = users.stream()
                .map(user -> new UserDto(
                        user.getId(),
                        user.getName(),
                        (user.getRoles() != null ? user.getRoles().stream()
                            .map(role -> role.getName().replace("ROLE_", ""))
                            .collect(Collectors.toSet()) : Collections.emptySet())))
                .collect(Collectors.toList());

        model.addAttribute("users", userDtos);
        return "accounts";
    }

    @PostMapping("/books/accounts/delete/{id}")
    public String deleteUserAccount(@PathVariable("id") Long id) {

        userService.deleteUserById(id);
        return "redirect:/books/accounts";
    }
}
