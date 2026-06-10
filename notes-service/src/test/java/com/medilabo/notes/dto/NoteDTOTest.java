package com.medilabo.notes.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class NoteDTOTest {

    @Test
    void shouldGetAndSetFields() {
        NoteDTO dto = new NoteDTO();
        LocalDateTime now = LocalDateTime.now();

        dto.setId("note1");
        dto.setPatientId(1);
        dto.setNote("Test note");
        dto.setCreatedAt(now);
        dto.setVersion(2L);

        assertEquals("note1", dto.getId());
        assertEquals(1, dto.getPatientId());
        assertEquals("Test note", dto.getNote());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(2L, dto.getVersion());
    }

    @Test
    void shouldCreateDtoWithConstructor() {
        LocalDateTime now = LocalDateTime.now();

        NoteDTO dto = new NoteDTO(
                "note1",
                1,
                "Test note",
                now
        );

        assertEquals("note1", dto.getId());
        assertEquals(1, dto.getPatientId());
        assertEquals("Test note", dto.getNote());
        assertEquals(now, dto.getCreatedAt());
    }
}