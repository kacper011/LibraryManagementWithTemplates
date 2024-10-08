package com.example.library.config;

import com.example.library.model.Role;
import com.example.library.model.User;
import com.example.library.repository.RoleRepository;
import com.example.library.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DataInitializerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private DataInitializer dataInitializer;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    @DisplayName("CommandLineRunner Creates Admin If Not Exists")
    @Test
    public void testCommandLineRunnerCreatesAdminIfNotExists() throws Exception{

        when(userRepository.findByName("admin")).thenReturn(Optional.empty());

        Role roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");

        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(roleAdmin);

        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        CommandLineRunner commandLineRunner = dataInitializer.commandLineRunner();
        assertDoesNotThrow(() -> commandLineRunner.run());

        verify(roleRepository).save(any(Role.class));

        verify(userRepository).save(argThat(user ->
                        user.getName().equals("admin") &&
                        user.getPassword().equals("encodedPassword") &&
                        user.getEmail().equals("admin@wp.pl") &&
                        user.getRoles().contains(roleAdmin)
                ));
    }

    @DisplayName("CommandLineRunner Does Not Create Admin If Exists")
    @Test
    public void testCommandLineRunnerDoesNotCreateAdminIfExists() throws Exception{

        User existingAdmin = new User();
        existingAdmin.setName("admin");

        when(userRepository.findByName("admin")).thenReturn(Optional.of(existingAdmin));

        CommandLineRunner commandLineRunner = dataInitializer.commandLineRunner();
        assertDoesNotThrow(() -> commandLineRunner.run());

        verify(userRepository, never()).save(any(User.class));
        verify(roleRepository, never()).save(any(Role.class));
    }
}