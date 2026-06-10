package com.medilabo.patient.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.core.MethodParameter;

import java.lang.reflect.Method;
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
                handler.handleNotFound(new NotFoundException("Patient not found"));

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Patient not found", response.getBody().get("message"));
        assertEquals(404, response.getBody().get("status"));
    }

    @Test
    void shouldHandleBadRequest() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleBadRequest(new IllegalArgumentException("Invalid patient"));

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Invalid patient", response.getBody().get("message"));
        assertEquals(400, response.getBody().get("status"));
    }

    @Test
    void shouldHandleGenericException() {
        ResponseEntity<Map<String, Object>> response =
                handler.handleException(new RuntimeException("Unexpected error"));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Internal server error", response.getBody().get("message"));
        assertEquals(500, response.getBody().get("status"));
    }

    @Test
    void shouldHandleOptimisticLocking() {
        ResponseEntity<String> response = handler.handleOptimisticLocking();

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(
                "La ressource a été modifiée par un autre utilisateur. Veuillez recharger la page.",
                response.getBody()
        );
    }

    @Test
    void shouldHandleValidationException() throws Exception {
        PatientValidationTarget target = new PatientValidationTarget();

        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(target, "patientDTO");

        bindingResult.addError(new FieldError(
                "patientDTO",
                "firstName",
                "First name is required"
        ));

        Method method = GlobalExceptionHandlerTest.class
                .getDeclaredMethod("dummyMethod", PatientValidationTarget.class);

        MethodParameter methodParameter = new MethodParameter(method, 0);

        MethodArgumentNotValidException exception =
                new MethodArgumentNotValidException(methodParameter, bindingResult);

        ResponseEntity<Map<String, Object>> response =
                handler.handleValidationException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation error", response.getBody().get("message"));
        assertEquals(400, response.getBody().get("status"));

        Map<String, String> errors =
                (Map<String, String>) response.getBody().get("errors");

        assertEquals("First name is required", errors.get("firstName"));
    }

    private void dummyMethod(PatientValidationTarget target) {
    }

    private static class PatientValidationTarget {
        private String firstName;

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }
    }
}