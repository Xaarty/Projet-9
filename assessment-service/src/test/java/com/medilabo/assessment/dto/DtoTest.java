package com.medilabo.assessment.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    @Test
    void shouldGetAndSetAssessmentResponseFields() {
        AssessmentResponseDTO dto = new AssessmentResponseDTO();

        dto.setPatientId(1);
        dto.setFirstName("Dra");
        dto.setLastName("A");
        dto.setAge(45);
        dto.setTriggerCount(2);
        dto.setAssessment("Borderline");
        dto.setMatchedTriggers(List.of("fumeur", "cholesterol"));

        assertEquals(1, dto.getPatientId());
        assertEquals("Dra", dto.getFirstName());
        assertEquals("A", dto.getLastName());
        assertEquals(45, dto.getAge());
        assertEquals(2, dto.getTriggerCount());
        assertEquals("Borderline", dto.getAssessment());
        assertEquals(2, dto.getMatchedTriggers().size());
    }

    @Test
    void shouldGetAndSetNoteFields() {
        NoteDTO dto = new NoteDTO();
        LocalDateTime now = LocalDateTime.now();

        dto.setId("note1");
        dto.setPatientId(1);
        dto.setNote("Test note");
        dto.setCreatedAt(now);

        assertEquals("note1", dto.getId());
        assertEquals(1, dto.getPatientId());
        assertEquals("Test note", dto.getNote());
        assertEquals(now, dto.getCreatedAt());
    }

}