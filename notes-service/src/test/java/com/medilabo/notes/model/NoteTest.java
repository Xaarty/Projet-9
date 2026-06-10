package com.medilabo.notes.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NoteTest {

    @Test
    void shouldGetAndSetFields() {
        Note note = new Note();
        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 4, 11, 25);

        note.setId("note-jules-x-07");
        note.setPatientId(623);
        note.setNote("Patient reports dizziness after physical effort.");
        note.setCreatedAt(createdAt);
        note.setVersion(3L);

        assertEquals("note-jules-x-07", note.getId());
        assertEquals(623, note.getPatientId());
        assertEquals("Patient reports dizziness after physical effort.", note.getNote());
        assertEquals(createdAt, note.getCreatedAt());
        assertEquals(3L, note.getVersion());
    }

    @Test
    void shouldCreateEntityWithConstructor() {
        LocalDateTime createdAt = LocalDateTime.of(2026, 6, 5, 15, 40);

        Note note = new Note(
                "note-chloe-t-08",
                734,
                "Blood pressure review. Weight management plan discussed.",
                createdAt
        );

        assertEquals("note-chloe-t-08", note.getId());
        assertEquals(734, note.getPatientId());
        assertEquals("Blood pressure review. Weight management plan discussed.", note.getNote());
        assertEquals(createdAt, note.getCreatedAt());
    }
}