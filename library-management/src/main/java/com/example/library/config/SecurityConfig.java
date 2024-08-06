package com.example.library.config;

import com.example.library.exception.ResourceNotFoundException;
import com.example.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final UserRepository userRepository;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> {
                    authorize
                            .requestMatchers("/books_admin").hasRole("ADMIN")
                            .requestMatchers("/books_user").hasRole("USER")
                            .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                            .requestMatchers("/login", "/login-error").permitAll()
                            .requestMatchers("/registration").permitAll()
                            .requestMatchers("/books/new").hasRole("ADMIN")
                            .requestMatchers("/books/{id}/edit").hasRole("ADMIN")
                            .requestMatchers("/books/{id}/delete").hasRole("ADMIN")
                            .requestMatchers("/my_books").hasRole("USER")
                            .requestMatchers("/books/{id}/rent").hasRole("USER")
                            .requestMatchers("/books/{id}/return").hasRole("USER")
                            .requestMatchers("/my_account").hasRole("USER")
                            .anyRequest().authenticated();
                })
                .formLogin(form -> {
                    form
                            .loginPage("/login")
                            .successHandler(customAuthenticationSuccessHandler)
                            .permitAll();
                })
                .logout(logout -> {
                    logout
                            .permitAll();
                })
                .csrf().disable();

        return http.build();

    }


    @Bean
    public UserDetailsService userDetailsService() {

        return username -> {
            var user = userRepository.findByName(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));

            var grantedAuthorities = user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName()))
                    .collect(Collectors.toSet());

            return new User(user.getName(), user.getPassword(), grantedAuthorities);

        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }





}
