package com.example.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorize -> {
                    authorize
                            .antMatchers("/css/**", "/js/**", "/images/**").permitAll()
                            .antMatchers("/login").permitAll()
                            .anyRequest().authenticated();
                })
                .formLogin(form -> {
                    form
                            .loginPage("/login")
                            .permitAll();
                })
                .logout(logout -> {
                    logout
                            .permitAll();
                })
                .csrf().disable(); // Wyłączenie CSRF dla testów - w produkcji powinno być włączone!
    }


    @Bean
    public UserDetailsService userDetailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        String userPassword = passwordEncoder().encode("user");
        String adminPassword = passwordEncoder().encode("admin");

        System.out.println("User Password: " + userPassword);
        System.out.println("Admin Password: " + adminPassword);

        manager.createUser(User.withUsername("user")
                .password(userPassword)
                .roles("USER")
                .build());
        manager.createUser(User.withUsername("admin")
                .password(adminPassword)
                .roles("ADMIN")
                .build());

        return manager;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }





}
