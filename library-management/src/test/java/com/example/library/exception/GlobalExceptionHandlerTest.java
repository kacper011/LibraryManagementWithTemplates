package com.example.library.exception;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        globalExceptionHandler = new GlobalExceptionHandler();
    }
    @DisplayName("Handle Todo Api Exception Should Return Bad Request Response")
    @Test
    void testHandleTodoAPIExceptionShouldReturnBadRequestResponse() {
        // Arrange
        TodoAPIException exception = new TodoAPIException(HttpStatus.BAD_REQUEST, "Test error");
        when(webRequest.getDescription(false)).thenReturn("Test request description");

        // Act
        ResponseEntity<ErrorDetails> responseEntity = globalExceptionHandler.handleTodoAPIException(exception, webRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
        assertEquals("Test error", responseEntity.getBody().getMessage());
        assertEquals("Test request description", responseEntity.getBody().getDetails());
        assertNotNull(responseEntity.getBody().getTimeStamp());
        assertTrue(responseEntity.getBody().getTimeStamp().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}

