package com.medilabo.assessment.service;

import com.medilabo.assessment.client.NotesClient;
import com.medilabo.assessment.client.PatientClient;
import com.medilabo.assessment.dto.AssessmentResponseDTO;
import com.medilabo.assessment.dto.NoteDTO;
import com.medilabo.assessment.dto.PatientDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AssessmentServiceTest {

    private PatientClient patientClient;
    private NotesClient notesClient;
    private AssessmentService assessmentService;

    @BeforeEach
    void setUp() {
        patientClient = mock(PatientClient.class);
        notesClient = mock(NotesClient.class);
        assessmentService = new AssessmentService(patientClient, notesClient);
    }

    @Test
    void shouldAssessPatient() {
        PatientDTO patient = new PatientDTO();
        patient.setId(1);
        patient.setFirstName("Didier");
        patient.setLastName("R");
        patient.setBirthDate(LocalDate.now().minusYears(45));
        patient.setGender("M");

        NoteDTO note1 = new NoteDTO();
        note1.setNote("Le patient est fumeur avec cholesterol.");

        NoteDTO note2 = new NoteDTO();
        note2.setNote("Vertiges signalés.");

        when(patientClient.getPatientById(1)).thenReturn(patient);
        when(notesClient.getNotesByPatientId(1)).thenReturn(List.of(note1, note2));

        AssessmentResponseDTO result = assessmentService.assessPatient(1);

        assertEquals(1, result.getPatientId());
        assertEquals("Didier", result.getFirstName());
        assertEquals("R", result.getLastName());
        assertEquals(45, result.getAge());
        assertEquals(3, result.getTriggerCount());
        assertEquals("Borderline", result.getAssessment());
        assertTrue(result.getMatchedTriggers().contains("fumeur"));
        assertTrue(result.getMatchedTriggers().contains("cholesterol"));
        assertTrue(result.getMatchedTriggers().contains("vertiges"));
    }

    @Test
    void shouldCalculateAge() {
        int age = assessmentService.calculateAge(LocalDate.now().minusYears(30));
        assertEquals(30, age);
    }

    @Test
    void shouldReturnEmptyTriggersWhenNotesAreNull() {
        List<String> result = assessmentService.findMatchedTriggers(null);
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldReturnEmptyTriggersWhenNotesAreEmpty() {
        List<String> result = assessmentService.findMatchedTriggers(List.of());
        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCountTriggers() {
        NoteDTO note = new NoteDTO();
        note.setNote("fumeur cholesterol vertiges");

        int result = assessmentService.countTriggers(List.of(note));

        assertEquals(3, result);
    }

    @Test
    void shouldReturnBorderlineWhenAgeOver30AndTwoTriggers() {
        assertEquals("Borderline", assessmentService.determineAssessment(45, "M", 2));
    }

    @Test
    void shouldReturnInDangerWhenAgeOver30AndSixTriggers() {
        assertEquals("InDanger", assessmentService.determineAssessment(45, "M", 6));
    }

    @Test
    void shouldReturnEarlyOnsetWhenAgeOver30AndEightTriggers() {
        assertEquals("EarlyOnset", assessmentService.determineAssessment(45, "M", 8));
    }

    @Test
    void shouldReturnNoneForManUnder30WithLessThanThreeTriggers() {
        assertEquals("None", assessmentService.determineAssessment(25, "M", 2));
    }

    @Test
    void shouldReturnInDangerForManUnder30WithThreeTriggers() {
        assertEquals("InDanger", assessmentService.determineAssessment(25, "M", 3));
    }

    @Test
    void shouldReturnEarlyOnsetForManUnder30WithFiveTriggers() {
        assertEquals("EarlyOnset", assessmentService.determineAssessment(25, "M", 5));
    }

    @Test
    void shouldReturnNoneForWomanUnder30WithLessThanFourTriggers() {
        assertEquals("None", assessmentService.determineAssessment(25, "F", 3));
    }

    @Test
    void shouldReturnInDangerForWomanUnder30WithFourTriggers() {
        assertEquals("InDanger", assessmentService.determineAssessment(25, "F", 4));
    }

    @Test
    void shouldReturnEarlyOnsetForWomanUnder30WithSevenTriggers() {
        assertEquals("EarlyOnset", assessmentService.determineAssessment(25, "F", 7));
    }
}