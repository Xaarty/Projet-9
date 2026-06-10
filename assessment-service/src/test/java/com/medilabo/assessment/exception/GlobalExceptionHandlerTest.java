package com.medilabo.assessment.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void shouldHandleBadRequest() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleBadRequest(new IllegalArgumentException("Invalid data"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid data", response.getBody().get("message"));
        assertEquals(400, response.getBody().get("status"));
    }

    @Test
    void shouldHandleNotFound() {

        HttpClientErrorException.NotFound ex =
                (HttpClientErrorException.NotFound)
                        HttpClientErrorException.create(
                                HttpStatus.NOT_FOUND,
                                "Not found",
                                null,
                                null,
                                null
                        );

        ResponseEntity<Map<String, Object>> response =
                handler.handleNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Requested resource was not found", response.getBody().get("message"));
        assertEquals(404, response.getBody().get("status"));
    }

    @Test
    void shouldHandleServiceUnavailable() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleServiceUnavailable(new ResourceAccessException("Service down"));

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("A required service is currently unavailable", response.getBody().get("message"));
        assertEquals(503, response.getBody().get("status"));
    }

    @Test
    void shouldHandleGenericException() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleException(new RuntimeException("Boom"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal server error", response.getBody().get("message"));
        assertEquals(500, response.getBody().get("status"));
    }
}