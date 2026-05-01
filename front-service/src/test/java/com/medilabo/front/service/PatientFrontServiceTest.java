package com.medilabo.front.service;

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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientFrontServiceTest {

    private RestTemplate restTemplate;
    private PatientFrontService patientFrontService;

    private PatientDTO patientDTO;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        patientFrontService = new PatientFrontService(restTemplate, "http://localhost:8080");

        patientDTO = new PatientDTO(
                1,
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "M",
                "1 Main Street",
                "0123456789"
        );
    }

    @Test
    void shouldReturnAllPatients() {
        PatientPageDTO patientPage = new PatientPageDTO();
        patientPage.setContent(List.of(patientDTO));
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
        assertEquals("John", result.getContent().get(0).getFirstName());
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
        ResponseEntity<PatientDTO> response =
                new ResponseEntity<>(patientDTO, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/patients/1"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(PatientDTO.class)
        )).thenReturn(response);

        PatientDTO result = patientFrontService.getPatientById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Doe", result.getLastName());
    }

    @Test
    void shouldCreatePatient() {
        ResponseEntity<PatientDTO> response =
                new ResponseEntity<>(patientDTO, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/patients"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(PatientDTO.class)
        )).thenReturn(response);

        patientFrontService.createPatient(patientDTO);

        verify(restTemplate).exchange(
                eq("http://localhost:8080/patients"),
                eq(HttpMethod.POST),
                any(HttpEntity.class),
                eq(PatientDTO.class)
        );
    }

    @Test
    void shouldUpdatePatient() {
        ResponseEntity<PatientDTO> response =
                new ResponseEntity<>(patientDTO, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/patients/1"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(PatientDTO.class)
        )).thenReturn(response);

        patientFrontService.updatePatient(1, patientDTO);

        verify(restTemplate).exchange(
                eq("http://localhost:8080/patients/1"),
                eq(HttpMethod.PUT),
                any(HttpEntity.class),
                eq(PatientDTO.class)
        );
    }

    @Test
    void shouldSearchPatientsByLastNameOnly() {
        PatientPageDTO patientPage = new PatientPageDTO();
        patientPage.setContent(List.of(patientDTO));
        patientPage.setNumber(0);
        patientPage.setSize(20);
        patientPage.setTotalElements(1);
        patientPage.setTotalPages(1);

        when(restTemplate.getForObject(
                "http://localhost:8080/patients/search?lastName=Doe&page=0&size=20",
                PatientPageDTO.class
        )).thenReturn(patientPage);

        PatientPageDTO result = patientFrontService.searchPatients("Doe", null, 0, 20);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Doe", result.getContent().get(0).getLastName());
    }

    @Test
    void shouldSearchPatientsByLastNameAndFirstName() {
        PatientPageDTO patientPage = new PatientPageDTO();
        patientPage.setContent(List.of(patientDTO));
        patientPage.setNumber(0);
        patientPage.setSize(20);
        patientPage.setTotalElements(1);
        patientPage.setTotalPages(1);

        when(restTemplate.getForObject(
                "http://localhost:8080/patients/search?lastName=Doe&page=0&size=20&firstName=John",
                PatientPageDTO.class
        )).thenReturn(patientPage);

        PatientPageDTO result = patientFrontService.searchPatients("Doe", "John", 0, 20);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("John", result.getContent().get(0).getFirstName());
    }

    @Test
    void shouldReturnEmptyPageWhenSearchPatientsResponseBodyIsNull() {
        when(restTemplate.getForObject(
                "http://localhost:8080/patients/search?lastName=Doe&page=0&size=20",
                PatientPageDTO.class
        )).thenReturn(null);

        PatientPageDTO result = patientFrontService.searchPatients("Doe", null, 0, 20);

        assertNotNull(result);
        assertTrue(result.getContent() == null || result.getContent().isEmpty());
    }

    @Test
    void shouldDeletePatient() {
        ResponseEntity<Void> response =
                new ResponseEntity<>(HttpStatus.NO_CONTENT);

        when(restTemplate.exchange(
                eq("http://localhost:8080/patients/1"),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Void.class)
        )).thenReturn(response);

        patientFrontService.deletePatient(1);

        verify(restTemplate).exchange(
                eq("http://localhost:8080/patients/1"),
                eq(HttpMethod.DELETE),
                any(HttpEntity.class),
                eq(Void.class)
        );
    }
}