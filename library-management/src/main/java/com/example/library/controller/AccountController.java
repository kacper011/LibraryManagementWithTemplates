package com.example.library.controller;

import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import com.example.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Base64;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class AccountController {

    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/my_account")
    public String myAccount(Model model, Principal principal) {
        Optional<User> optionalUser = userRepository.findByName(principal.getName());

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            String decodedPassword;
            try {
                decodedPassword = new String(Base64.getDecoder().decode(user.getPassword()));
            } catch (IllegalArgumentException e) {
                decodedPassword = user.getPassword();
            }

            model.addAttribute("username", user.getName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("password", decodedPassword);
        } else {
            model.addAttribute("error", "User not found");
        }

        return "my_account";
    }

    @PostMapping("/my_account/update_profile")
    public String updateProfile(@RequestParam("username") String newUsername,
                                @RequestParam("email") String newEmail,
                                @RequestParam("password") String newPassword,
                                Authentication authentication) {
        String currentUsername = authentication.getName();
        Optional<User> userOptional = userRepository.findByName(currentUsername);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            boolean updated = false;

            if (!user.getName().equals(newUsername)) {
                user.setName(newUsername);
                updated = true;
            }

            if (!user.getEmail().equals(newEmail)) {
                user.setEmail(newEmail);
                updated = true;
            }

            if (!user.getPassword().equals(newPassword)) {
                user.setPassword(newPassword);
                updated = true;
            }

            if (updated) {
                userRepository.save(user);
            }

            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(newUsername, authentication.getCredentials(), authentication.getAuthorities())
            );

            return "redirect:/my_account?success";
        } else {
            return "redirect:/my_account?error";
        }
    }



    private String decodePassword(String encodedPassword) {
        return new String(Base64.getDecoder().decode(encodedPassword));
    }
}
