package com.example.library.controller;

import com.example.library.model.User;
import com.example.library.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
class RegistrationControllerTest {

    @Test
    public void showRegistrationFormTest() {

        //Given
        UserService userService = Mockito.mock(UserService.class);
        RegistrationController registrationController = new RegistrationController(userService, rabbitTemplate);
        Model model = Mockito.mock(Model.class);

        //When
        String viewName = registrationController.showRegistrationForm(model);

        //Then
        assertEquals("registration", viewName);
        verify(model).addAttribute(Mockito.eq("user"), Mockito.any(User.class));
    }

}