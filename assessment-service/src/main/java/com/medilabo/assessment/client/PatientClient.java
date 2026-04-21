package com.medilabo.assessment.client;

import com.medilabo.assessment.dto.PatientDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PatientClient {

    private final RestTemplate restTemplate;

    public PatientClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${patient-service.base-url}")
    private String patientServiceBaseUrl;

    public PatientDTO getPatientById(Integer patientId) {
        return restTemplate.getForObject(
                patientServiceBaseUrl + "/patients/" + patientId,
                PatientDTO.class
        );
    }
}