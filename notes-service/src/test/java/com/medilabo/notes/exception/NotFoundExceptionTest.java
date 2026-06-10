package com.medilabo.notes.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {

    @Test
    void shouldStoreMessage() {
        NotFoundException exception =
                new NotFoundException("Note not found");

        assertEquals(
                "Note not found",
                exception.getMessage()
        );
    }
}