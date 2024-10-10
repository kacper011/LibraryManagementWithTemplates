package com.example.library.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class PasswordEncoderImplTest {

    @DisplayName("Encode Password Should Return Different Results")
    @Test
    public void testEncodePasswordShouldReturnDifferentResults() {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = "admin";

        String encodedPassword1 = passwordEncoder.encode(password);
        String encodedPassword2 = passwordEncoder.encode(password);

        assertNotEquals(encodedPassword1, encodedPassword2);
    }

    @DisplayName("Encoded Password Matches")
    @Test
    public void testEncodedPasswordMatches() {

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String rawPassword = "admin";

        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertTrue(passwordEncoder.matches(rawPassword, encodedPassword));
    }
}