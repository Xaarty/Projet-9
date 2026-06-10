package com.medilabo.front.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;
    private Model model;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
        model = mock(Model.class);
    }

    @Test
    void shouldHandleNotFound() {
        String view = handler.handleNotFound(null, model);

        assertEquals("error", view);
        verify(model).addAttribute("errorTitle", "Resource not found");
        verify(model).addAttribute("errorMessage", "The requested resource could not be found.");
    }

    @Test
    void shouldHandleServiceUnavailable() {
        String view = handler.handleServiceUnavailable(null, model);

        assertEquals("error", view);
        verify(model).addAttribute("errorTitle", "Service unavailable");
        verify(model).addAttribute("errorMessage", "A required backend service is currently unavailable.");
    }

    @Test
    void shouldHandleBadRequest() {
        String view = handler.handleBadRequest(new IllegalArgumentException("Invalid data"), model);

        assertEquals("error", view);
        verify(model).addAttribute("errorTitle", "Invalid request");
        verify(model).addAttribute("errorMessage", "Invalid data");
    }

    @Test
    void shouldHandleGenericException() {
        String view = handler.handleException(new RuntimeException("Boom"), model);

        assertEquals("error", view);
        verify(model).addAttribute("errorTitle", "Internal server error");
        verify(model).addAttribute("errorMessage", "An unexpected error occurred.");
    }
}