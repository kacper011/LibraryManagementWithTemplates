package com.example.library;

import com.example.library.model.Role;
import com.example.library.model.User;
import com.example.library.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

@SpringBootApplication
public class LibraryManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementApplication.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepository.count() != 0) {
				return;
			}
			Role userRole = Role.builder()
					.name("USER")
					.build();
			Role adminRole = Role.builder()
					.name("ADMIN")
					.build();
			User user = User.builder()
					.name("user")
					.password(passwordEncoder.encode("user"))
					.email("user@email.com")
					.roles(Set.of(userRole))
					.build();
			User admin = User.builder()
					.name("admin")
					.password(passwordEncoder.encode("admin"))
					.email("admin@email.com")
					.roles(Set.of(adminRole))
					.build();
			userRepository.save(user);
			userRepository.save(admin);
		};
	}

}
