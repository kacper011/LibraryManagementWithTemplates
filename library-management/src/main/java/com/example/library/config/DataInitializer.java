package com.example.library.config;

import com.example.library.model.Role;
import com.example.library.model.User;
import com.example.library.repository.RoleRepository;
import com.example.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            if (userRepository.findByName("admin").isEmpty()) {
                Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                        .orElseGet(() -> {
                            Role newAdminRole = new Role();
                            newAdminRole.setName("ROLE_ADMIN");
                            return roleRepository.save(newAdminRole);
                        });

                User admin = new User();
                admin.setName("admin");
                admin.setPassword(passwordEncoder.encode("adminPassword"));
                admin.setEmail("admin@wp.pl");

                Set<Role> roles = new HashSet<>();
                roles.add(adminRole);
                admin.setRoles(roles);


                userRepository.save(admin);
            }
        };
    }
}
