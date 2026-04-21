package com.medilabo.notes.exception;

// Exception métier lorsqu’une note n’existe pas
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}