package com.example.library.config;

import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CustomAuthenticationSuccessHandlerTest {

    @Mock
    private Authentication authentication;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private AuthenticationSuccessHandler successHandler;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        successHandler = new CustomAuthenticationSuccessHandler();
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @DisplayName("Admin Redirect")
    @Test
    public void testAdminRedirect() throws IOException, ServletException {

        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));

        when(authentication.getAuthorities()).thenReturn((Collection) authorities);
        when(authentication.getName()).thenReturn("adminUser");

        successHandler.onAuthenticationSuccess(request, response, authentication);

        assertEquals("/books_admin", response.getRedirectedUrl());
    }

    @DisplayName("User Redirect")
    @Test
    public void testUserRedirect() throws IOException, ServletException {

        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        when(authentication.getName()).thenReturn("regularUser");

        successHandler.onAuthenticationSuccess(request, response, authentication);

        assertEquals("/books_user", response.getRedirectedUrl());
    }

    @DisplayName("Access Denied Redirect")
    @Test
    public void testAccessDeniedRedirect() throws IOException, ServletException {

        Collection<GrantedAuthority> authorities = Collections.emptyList();

        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        when(authentication.getName()).thenReturn("noRoleUser");

        successHandler.onAuthenticationSuccess(request, response, authentication);

        assertEquals("/access_denied", response.getRedirectedUrl());
    }

}