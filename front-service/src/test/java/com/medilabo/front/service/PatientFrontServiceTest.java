package com.medilabo.front.service;

import com.medilabo.front.dto.PatientDTO;
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
        ResponseEntity<List<PatientDTO>> response =
                new ResponseEntity<>(List.of(patientDTO), HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/patients"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        List<PatientDTO> result = patientFrontService.getAllPatients();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void shouldReturnEmptyListWhenGetAllPatientsResponseBodyIsNull() {
        ResponseEntity<List<PatientDTO>> response =
                new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/patients"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        List<PatientDTO> result = patientFrontService.getAllPatients();

        assertNotNull(result);
        assertTrue(result.isEmpty());
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
        ResponseEntity<List<PatientDTO>> response =
                new ResponseEntity<>(List.of(patientDTO), HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/patients/search?lastName=Doe"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        List<PatientDTO> result = patientFrontService.searchPatients("Doe", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Doe", result.get(0).getLastName());
    }

    @Test
    void shouldSearchPatientsByLastNameAndFirstName() {
        ResponseEntity<List<PatientDTO>> response =
                new ResponseEntity<>(List.of(patientDTO), HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/patients/search?lastName=Doe&firstName=John"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        List<PatientDTO> result = patientFrontService.searchPatients("Doe", "John");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
    }

    @Test
    void shouldReturnEmptyListWhenSearchPatientsResponseBodyIsNull() {
        ResponseEntity<List<PatientDTO>> response =
                new ResponseEntity<>(null, HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8080/patients/search?lastName=Doe"),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        List<PatientDTO> result = patientFrontService.searchPatients("Doe", null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
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