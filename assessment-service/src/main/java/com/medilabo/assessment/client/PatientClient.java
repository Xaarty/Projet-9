package com.medilabo.assessment.client;

import com.medilabo.assessment.dto.PatientDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PatientClient {

    private final RestTemplate restTemplate;

    public PatientClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public PatientDTO getPatientById(Integer patientId) {
        return restTemplate.getForObject(
                "http://localhost:8081/patients/" + patientId,
                PatientDTO.class
        );
    }
}