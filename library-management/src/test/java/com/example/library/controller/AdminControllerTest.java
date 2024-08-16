package com.example.library.controller;

import com.example.library.model.Role;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class AdminControllerTest {

    @InjectMocks
    private AdminController adminController;
    @Mock
    private UserRepository userRepository;
    @Mock
    private Model model;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void listUserAccountsShouldReturnAccountViewNameTest() {

        User user1 = new User("Alice", new HashSet<>(Set.of(new Role("ROLE_USER"))));
        User user2 = new User("Bob", new HashSet<>(Set.of(new Role("ROLE_ADMIN"))));

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        String viewName = adminController.listUserAccounts(model);

        assertThat(viewName).isEqualTo("accounts");
        verify(model).addAttribute(eq("users"), any());
    }

}