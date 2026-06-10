package com.medilabo.assessment.client;

import com.medilabo.assessment.dto.PatientDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientClientTest {

    private RestTemplate restTemplate;
    private PatientClient patientClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        patientClient = new PatientClient(restTemplate);
        ReflectionTestUtils.setField(patientClient, "patientServiceBaseUrl", "http://localhost:8081");
    }

    @Test
    void shouldReturnPatientById() {
        PatientDTO patient = new PatientDTO();
        patient.setId(1);
        patient.setFirstName("Fred");

        when(restTemplate.getForObject(
                "http://localhost:8081/patients/1",
                PatientDTO.class
        )).thenReturn(patient);

        PatientDTO result = patientClient.getPatientById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Fred", result.getFirstName());
    }
}