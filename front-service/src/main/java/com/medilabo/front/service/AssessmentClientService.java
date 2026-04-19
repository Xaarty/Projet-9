package com.medilabo.front.service;

import com.medilabo.front.dto.AssessmentDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AssessmentClientService {

    private final RestTemplate restTemplate;
    private final String gatewayBaseUrl = "http://localhost:8080";

    public AssessmentClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public AssessmentDTO getAssessmentByPatientId(Integer patientId) {
        return restTemplate.getForObject(
                gatewayBaseUrl + "/assess/" + patientId,
                AssessmentDTO.class
        );
    }
}