package com.medilabo.front.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

@ControllerAdvice
public class GlobalExceptionHandler {

    //Erreur 404 lorsqu'une ressource est introuvable
    @ExceptionHandler(HttpClientErrorException.NotFound.class)
    public String handleNotFound(HttpClientErrorException.NotFound ex, Model model) {
        model.addAttribute("errorTitle", "Resource not found");
        model.addAttribute("errorMessage", "The requested resource could not be found.");
        return "error";
    }

    //Erreur 503 lorsqu'un appel a un service externe est indisponible
    @ExceptionHandler(ResourceAccessException.class)
    public String handleServiceUnavailable(ResourceAccessException ex, Model model) {
        model.addAttribute("errorTitle", "Service unavailable");
        model.addAttribute("errorMessage", "A required backend service is currently unavailable.");
        return "error";
    }

    //Erreur 400 lorsqu’une donnée d’entrée est invalide (logique métier)
    @ExceptionHandler(IllegalArgumentException.class)
    public String handleBadRequest(IllegalArgumentException ex, Model model) {
        model.addAttribute("errorTitle", "Invalid request");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }

    //Erreur 500 pour toute erreur non répertorié
    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Internal server error");
        model.addAttribute("errorMessage", "An unexpected error occurred.");
        return "error";
    }
}