package com.medilabo.front.service;

import com.medilabo.front.dto.AssessmentDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AssessmentClientService {

    private final RestTemplate restTemplate;
    private final String gatewayBaseUrl;

    public AssessmentClientService(RestTemplate restTemplate,
                                   @Value("${gateway.base-url}") String gatewayBaseUrl) {
        this.restTemplate = restTemplate;
        this.gatewayBaseUrl = gatewayBaseUrl;
    }

    public AssessmentDTO getAssessmentByPatientId(Integer patientId) {
        return restTemplate.getForObject(
                gatewayBaseUrl + "/assess/" + patientId,
                AssessmentDTO.class
        );
    }
}