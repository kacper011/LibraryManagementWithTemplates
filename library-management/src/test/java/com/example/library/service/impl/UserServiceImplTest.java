package com.example.library.service.impl;

import com.example.library.exception.ResourceNotFoundException;
import com.example.library.model.Role;
import com.example.library.model.User;
import com.example.library.repository.RoleRepository;
import com.example.library.repository.UserRepository;
import com.example.library.service.UserService;
import jakarta.persistence.Table;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private RoleRepository roleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }


    @DisplayName("Save User Success")
    @Test
    public void testSaveUserSuccess() {

        User user = new User();
        user.setPassword("testPassword");

        Role userRole = new Role();
        userRole.setName("ROLE_USER");

        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode("testPassword")).thenReturn("encodedPassword");

        userServiceImpl.saveUser(user);

        assertEquals("encodedPassword", user.getPassword());
        assertEquals(Set.of(userRole), user.getRoles());

        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(passwordEncoder, times(1)).encode("testPassword");
        verify(userRepository, times(1)).save(user);
    }

    @DisplayName("Save User Role Not Found")
    @Test
    public void testSaveUserRoleNotFound() {

        User user = new User();

        when(roleRepository.findByName("ROLE_USER")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userServiceImpl.saveUser(user);
        });

        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("Update Email Success")
    @Test
    public void testUpdateEmailSuccess() {

        Long userId = 1L;
        String newEmail = "newemail@gmail.com";
        User user = new User();
        user.setId(userId);
        user.setEmail("oldemail@gmail.com");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userServiceImpl.updateEmail(userId, newEmail);

        assertEquals(newEmail, user.getEmail());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }

    @DisplayName("Update Email User Not Found")
    @Test
    public void testUpdateEmailUserNotFound() {

        Long userId = 1L;
        String newEmail = "newemail@gmail.com";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userServiceImpl.updateEmail(userId, newEmail);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @DisplayName("Update Username Success")
    @Test
    public void testUpdateUsernameSuccess() {

        Long userId = 1L;
        String newUsername = "newUsername";
        User user = new User();
        user.setId(userId);
        user.setName("oldUsername");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        userServiceImpl.updateUsername(userId, newUsername);

        assertEquals(newUsername, user.getName());

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(user);
    }

    @DisplayName("Update Username User Not Found")
    @Test
    public void testUpdateUsernameUserNotFound() {

        Long userId = 1L;
        String newUsername = "newUsername";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            userServiceImpl.updateUsername(userId, newUsername);
        });

        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

}