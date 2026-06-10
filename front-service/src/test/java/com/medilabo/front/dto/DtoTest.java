package com.medilabo.front.dto;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    @Test
    void shouldGetAndSetAssessmentDTOFields() {
        AssessmentDTO dto = new AssessmentDTO();

        dto.setPatientId(1);
        dto.setFirstName("Fred");
        dto.setLastName("RR");
        dto.setAge(45);
        dto.setTriggerCount(3);
        dto.setAssessment("InDanger");
        dto.setMatchedTriggers(List.of("smoker", "abnormal"));

        assertEquals(1, dto.getPatientId());
        assertEquals("Fred", dto.getFirstName());
        assertEquals("RR", dto.getLastName());
        assertEquals(45, dto.getAge());
        assertEquals(3, dto.getTriggerCount());
        assertEquals("InDanger", dto.getAssessment());
        assertEquals(2, dto.getMatchedTriggers().size());
    }

    @Test
    void shouldGetAndSetNotesDTOFields() {
        NotesDTO dto = new NotesDTO();
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

    @Test
    void shouldGetAndSetPatientDTOFields() {
        PatientDTO dto = new PatientDTO();

        dto.setId(1);
        dto.setFirstName("Leslie");
        dto.setLastName("F");
        dto.setBirthDate(LocalDate.of(1990, 1, 1));
        dto.setGender("F");
        dto.setAddress("Rue 1");
        dto.setPhone("0123456789");
        dto.setVersion(1L);

        assertEquals(1, dto.getId());
        assertEquals("Leslie", dto.getFirstName());
        assertEquals("F", dto.getLastName());
        assertEquals(LocalDate.of(1990, 1, 1), dto.getBirthDate());
        assertEquals("F", dto.getGender());
        assertEquals("Rue 1", dto.getAddress());
        assertEquals("0123456789", dto.getPhone());
        assertEquals(1L, dto.getVersion());
    }

    @Test
    void shouldEvaluatePatientPageNavigation() {
        PatientPageDTO dto = new PatientPageDTO();

        dto.setContent(List.of(new PatientDTO()));
        dto.setNumber(1);
        dto.setSize(20);
        dto.setTotalElements(40);
        dto.setTotalPages(3);

        assertEquals(1, dto.getContent().size());
        assertEquals(1, dto.getNumber());
        assertEquals(20, dto.getSize());
        assertEquals(40, dto.getTotalElements());
        assertEquals(3, dto.getTotalPages());
        assertTrue(dto.isHasPrevious());
        assertTrue(dto.isHasNext());
    }

    @Test
    void shouldReturnFalseWhenNoPreviousOrNextPage() {
        PatientPageDTO dto = new PatientPageDTO();

        dto.setNumber(0);
        dto.setTotalPages(1);

        assertFalse(dto.isHasPrevious());
        assertFalse(dto.isHasNext());
    }
}