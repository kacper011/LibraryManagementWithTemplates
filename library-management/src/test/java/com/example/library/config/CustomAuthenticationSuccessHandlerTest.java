package com.example.library.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

import static org.mockito.Mockito.*;

class CustomAuthenticationSuccessHandlerTest {

    private CustomAuthenticationSuccessHandler handler;

    private HttpServletRequest request;
    private HttpServletResponse response;
    private Authentication authentication;
    private HttpSession session;

    @BeforeEach
    public void setup() {

        handler = new CustomAuthenticationSuccessHandler();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        authentication = mock(Authentication.class);
        session = mock(HttpSession.class);

        when(request.getSession()).thenReturn(session);
    }

    @DisplayName("Authentication Success Admin Role Should Redirect To Admin Page")
    @Test
    public void testAuthenticationSuccessAdminRoleShouldRedirectToAdminPage() throws IOException, ServletException {

        when(authentication.getName()).thenReturn("admin");
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        handler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/books_admin");

    }

    @DisplayName("Authentication Success User Role Should Redirect To User Page")
    @Test
    public void testAuthenticationSuccessUserRoleShouldRedirectToUserPage() throws IOException, ServletException {

        when(authentication.getName()).thenReturn("user");
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        handler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/books_user");
    }

    @DisplayName("Authentication Success No Role Should Redirect To Access Denied")
    @Test
    public void testAuthenticationSuccessNoRoleShouldRedirectToAccessDenied() throws IOException, ServletException {

        when(authentication.getName()).thenReturn("guest");
        Collection<GrantedAuthority> authorities = Collections.emptyList();
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        handler.onAuthenticationSuccess(request, response, authentication);

        verify(response).sendRedirect("/access_denied");
    }

    @DisplayName("Authentication Success With Session Should Print Session Info")
    @Test
    public void testAuthenticationSuccessWithSessionShouldPrintSessionInfo() throws IOException, ServletException {

        when(authentication.getName()).thenReturn("user");
        Collection<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        when(authentication.getAuthorities()).thenReturn((Collection) authorities);

        handler.onAuthenticationSuccess(request, response, authentication);

        verify(session).getId();
        verify(response).sendRedirect("/books_user");
    }

}