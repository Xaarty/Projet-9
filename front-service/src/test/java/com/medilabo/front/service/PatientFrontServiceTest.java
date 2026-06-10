package com.medilabo.front.service;

import com.medilabo.front.dto.AssessmentDTO;
import com.medilabo.front.dto.NotesDTO;
import com.medilabo.front.dto.PatientDTO;
import com.medilabo.front.dto.PatientPageDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientFrontServiceTest {

    private RestTemplate restTemplate;
    private PatientFrontService patientFrontService;

    private PatientDTO lucasPatient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        patientFrontService = new PatientFrontService(restTemplate, "http://localhost:8080");

        lucasPatient = new PatientDTO(
                101,
                "Lucas",
                "B",
                LocalDate.of(1988, 5, 12),
                "M",
                "10 Oak Street",
                "0102030405"
        );
    }

    @Test
    void shouldReturnAllPatients() {
        PatientPageDTO patientPage = new PatientPageDTO();
        patientPage.setContent(List.of(lucasPatient));
        patientPage.setNumber(0);
        patientPage.setSize(20);
        patientPage.setTotalElements(1);
        patientPage.setTotalPages(1);

        when(restTemplate.getForObject(
                "http://localhost:8080/patients?page=0&size=20",
                PatientPageDTO.class
        )).thenReturn(patientPage);

        PatientPageDTO result = patientFrontService.getAllPatients(0, 20);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Lucas", result.getContent().get(0).getFirstName());
        assertEquals("B", result.getContent().get(0).getLastName());
    }

    @Test
    void shouldReturnEmptyPageWhenGetAllPatientsResponseBodyIsNull() {
        when(restTemplate.getForObject(
                "http://localhost:8080/patients?page=0&size=20",
                PatientPageDTO.class
        )).thenReturn(null);

        PatientPageDTO result = patientFrontService.getAllPatients(0, 20);

        assertNotNull(result);
        assertTrue(result.getContent() == null || result.getContent().isEmpty());
    }

    @Test
    void shouldReturnPatientById() {
        PatientDTO emmaPatient = new PatientDTO(
                205,
                "Emma",
                "K",
                LocalDate.of(1995, 8, 3),
                "F",
                "22 Pine Avenue",
                "0607080910"
        );

        ResponseEntity<PatientDTO> response =
                new ResponseEntity<>(emmaPatient, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/patients/205"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(PatientDTO.class)
        )).thenReturn(response);

        PatientDTO result = patientFrontService.getPatientById(205);

        assertNotNull(result);
        assertEquals(205, result.getId());
        assertEquals("Emma", result.getFirstName());
        assertEquals("K", result.getLastName());
    }

    @Test
    void shouldCreatePatient() {
        PatientDTO noahPatient = new PatientDTO(
                309,
                "Noah",
                "RT",
                LocalDate.of(1979, 11, 20),
                "M",
                "5 River Road",
                "0111222333"
        );

        ResponseEntity<PatientDTO> response =
                new ResponseEntity<>(noahPatient, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/patients"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(PatientDTO.class)
        )).thenReturn(response);

        patientFrontService.createPatient(noahPatient);

        verify(restTemplate).exchange(
                eq("http://localhost:8080/patients"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(PatientDTO.class)
        );
    }

    @Test
    void shouldUpdatePatient() {
        PatientDTO leaPatient = new PatientDTO(
                412,
                "Lea",
                "V",
                LocalDate.of(2001, 3, 14),
                "F",
                "14 Lake Street",
                "0708091011"
        );

        ResponseEntity<PatientDTO> response =
                new ResponseEntity<>(leaPatient, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/patients/412"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(PatientDTO.class)
        )).thenReturn(response);

        patientFrontService.updatePatient(412, leaPatient);

        verify(restTemplate).exchange(
                eq("http://localhost:8080/patients/412"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(PatientDTO.class)
        );
    }

    @Test
    void shouldSearchPatientsByLastNameOnly() {
        PatientDTO hugoPatient = new PatientDTO(
                518,
                "Hugo",
                "M",
                LocalDate.of(1983, 6, 9),
                "M",
                "18 Forest Lane",
                "0203040506"
        );

        PatientPageDTO patientPage = new PatientPageDTO();
        patientPage.setContent(List.of(hugoPatient));
        patientPage.setNumber(0);
        patientPage.setSize(20);
        patientPage.setTotalElements(1);
        patientPage.setTotalPages(1);

        when(restTemplate.getForObject(
                "http://localhost:8080/patients/search?lastName=M&page=0&size=20",
                PatientPageDTO.class
        )).thenReturn(patientPage);

        PatientPageDTO result = patientFrontService.searchPatients("M", null, 0, 20);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Hugo", result.getContent().get(0).getFirstName());
        assertEquals("M", result.getContent().get(0).getLastName());
    }

    @Test
    void shouldSearchPatientsByLastNameAndFirstName() {
        PatientDTO claraPatient = new PatientDTO(
                623,
                "Clara",
                "Z",
                LocalDate.of(1992, 12, 1),
                "F",
                "7 Hill Road",
                "0304050607"
        );

        PatientPageDTO patientPage = new PatientPageDTO();
        patientPage.setContent(List.of(claraPatient));
        patientPage.setNumber(0);
        patientPage.setSize(20);
        patientPage.setTotalElements(1);
        patientPage.setTotalPages(1);

        when(restTemplate.getForObject(
                "http://localhost:8080/patients/search?lastName=Z&page=0&size=20&firstName=Clara",
                PatientPageDTO.class
        )).thenReturn(patientPage);

        PatientPageDTO result = patientFrontService.searchPatients("Z", "Clara", 0, 20);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Clara", result.getContent().get(0).getFirstName());
        assertEquals("Z", result.getContent().get(0).getLastName());
    }

    @Test
    void shouldReturnEmptyPageWhenSearchPatientsResponseBodyIsNull() {
        when(restTemplate.getForObject(
                "http://localhost:8080/patients/search?lastName=P&page=0&size=20",
                PatientPageDTO.class
        )).thenReturn(null);

        PatientPageDTO result = patientFrontService.searchPatients("P", null, 0, 20);

        assertNotNull(result);
        assertTrue(result.getContent() == null || result.getContent().isEmpty());
    }

    @Test
    void shouldDeletePatient() {
        ResponseEntity<Void> response =
                new ResponseEntity<>(HttpStatus.NO_CONTENT);

        when(restTemplate.exchange(
                eq("http://localhost:8080/patients/734"),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Void.class)
        )).thenReturn(response);

        patientFrontService.deletePatient(734);

        verify(restTemplate).exchange(
                eq("http://localhost:8080/patients/734"),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Void.class)
        );
    }

    @Test
    void shouldReturnNotesByPatientId() {
        NotesDTO note = new NotesDTO(
                "note-alice-1",
                845,
                "Routine health assessment.",
                LocalDateTime.of(2026, 4, 21, 10, 30)
        );

        ResponseEntity<List<NotesDTO>> response =
                new ResponseEntity<>(List.of(note), HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/notes/patient/845"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        List<NotesDTO> result = patientFrontService.getNotesByPatientId(845);

        assertEquals(1, result.size());
        assertEquals("Routine health assessment.", result.get(0).getNote());
    }

    @Test
    void shouldReturnEmptyNotesWhenBodyIsNull() {
        ResponseEntity<List<NotesDTO>> response =
                new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/notes/patient/956"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        List<NotesDTO> result = patientFrontService.getNotesByPatientId(956);

        assertTrue(result.isEmpty());
    }

    @Test
    void shouldCreateNote() {
        NotesDTO note = new NotesDTO();
        note.setPatientId(106);
        note.setNote("Smoking cessation discussed.");

        when(restTemplate.exchange(
                eq("http://localhost:8080/notes"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(NotesDTO.class)
        )).thenReturn(new ResponseEntity<>(note, HttpStatus.OK));

        patientFrontService.createNote(note);

        verify(restTemplate).exchange(
                eq("http://localhost:8080/notes"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(NotesDTO.class)
        );
    }

    @Test
    void shouldReturnAssessmentByPatientId() {
        AssessmentDTO assessment = new AssessmentDTO();
        assessment.setPatientId(217);
        assessment.setFirstName("Ethan");
        assessment.setLastName("P");
        assessment.setAssessment("InDanger");

        when(restTemplate.getForObject(
                "http://localhost:8080/assess/217",
                AssessmentDTO.class
        )).thenReturn(assessment);

        AssessmentDTO result = patientFrontService.getAssessmentByPatientId(217);

        assertNotNull(result);
        assertEquals("Ethan", result.getFirstName());
        assertEquals("P", result.getLastName());
        assertEquals("InDanger", result.getAssessment());
    }

    @Test
    void shouldThrowExceptionWhenUpdatePatientFails() {
        PatientDTO alicePatient = new PatientDTO(
                328,
                "Alice",
                "D",
                LocalDate.of(1986, 7, 19),
                "F",
                "9 Cedar Avenue",
                "0405060708"
        );

        ResponseEntity<PatientDTO> response =
                new ResponseEntity<>(alicePatient, HttpStatus.BAD_REQUEST);

        when(restTemplate.exchange(
                eq("http://localhost:8080/patients/328"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(PatientDTO.class)
        )).thenReturn(response);

        assertThrows(RuntimeException.class, () -> {
            patientFrontService.updatePatient(328, alicePatient);
        });
    }

    @Test
    void shouldReturnNoteById() {
        NotesDTO note = new NotesDTO();
        note.setId("note-jules-1");
        note.setPatientId(439);
        note.setNote("Weight management plan.");

        when(restTemplate.getForObject(
                "http://localhost:8080/notes/note-jules-1",
                NotesDTO.class
        )).thenReturn(note);

        NotesDTO result = patientFrontService.getNoteById("note-jules-1");

        assertNotNull(result);
        assertEquals("note-jules-1", result.getId());
        assertEquals("Weight management plan.", result.getNote());
    }

    @Test
    void shouldUpdateNote() {
        NotesDTO note = new NotesDTO();
        note.setId("note-chloe-1");
        note.setPatientId(540);
        note.setNote("Blood pressure review.");

        when(restTemplate.getForObject(
                "http://localhost:8080/notes/note-chloe-1",
                NotesDTO.class
        )).thenReturn(note);

        NotesDTO result = patientFrontService.updateNote("note-chloe-1", note);

        verify(restTemplate).put("http://localhost:8080/notes/note-chloe-1", note);
        assertEquals("Blood pressure review.", result.getNote());
    }
}