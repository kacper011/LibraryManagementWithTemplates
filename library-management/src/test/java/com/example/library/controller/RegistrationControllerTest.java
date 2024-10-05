package com.example.library.controller;

import com.example.library.model.User;
import com.example.library.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
class RegistrationControllerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @DisplayName("Show Registration Form")
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