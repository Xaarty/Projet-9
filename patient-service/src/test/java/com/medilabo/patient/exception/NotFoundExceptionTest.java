package com.medilabo.patient.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NotFoundExceptionTest {

    @Test
    void shouldStoreMessage() {
        NotFoundException exception =
                new NotFoundException("Patient not found");

        assertEquals("Patient not found", exception.getMessage());
    }
}