package com.medilabo.notes.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleNotFound() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleNotFound(new NotFoundException("Note not found"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Note not found", response.getBody().get("message"));
        assertEquals(404, response.getBody().get("status"));
    }

    @Test
    void shouldHandleBadRequest() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleBadRequest(new IllegalArgumentException("Invalid data"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid data", response.getBody().get("message"));
        assertEquals(400, response.getBody().get("status"));
    }

    @Test
    void shouldHandleGenericException() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleException(new RuntimeException("Boom"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody().get("message"));
        assertEquals(500, response.getBody().get("status"));
    }

    @Test
    void shouldHandleOptimisticLocking() {
        ResponseEntity<String> response = handler.handleOptimisticLocking();

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("La note a été modifiée par un autre utilisateur. Veuillez recharger la page.", response.getBody());
    }
}