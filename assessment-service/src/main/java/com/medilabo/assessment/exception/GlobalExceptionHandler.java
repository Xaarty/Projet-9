package com.medilabo.assessment.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    //Erreur 400 lorsqu’une donnée d’entrée est invalide (logique métier)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleBadRequest(IllegalArgumentException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", ex.getMessage());
        error.put("status", HttpStatus.BAD_REQUEST.value());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    //Erreur 404 lorsqu'une ressource est introuvable
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(HttpClientErrorException.NotFound ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", "Requested resource was not found");
        error.put("status", HttpStatus.NOT_FOUND.value());

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    //Erreur 503 lorsqu'une tentative d'utilisation d'un sevice externe est indisponible
    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<Map<String, Object>> handleServiceUnavailable(ResourceAccessException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", "A required service is currently unavailable");
        error.put("status", HttpStatus.SERVICE_UNAVAILABLE.value());

        return new ResponseEntity<>(error, HttpStatus.SERVICE_UNAVAILABLE);
    }

    //Erreur 500 pour toute erreur non répertorié
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("message", "Internal server error");
        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}