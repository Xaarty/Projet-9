package com.medilabo.front.service;

import com.medilabo.front.dto.PatientDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class PatientFrontService {

    private final RestTemplate restTemplate;
    private final String gatewayBaseUrl;

    public PatientFrontService(RestTemplate restTemplate,
                               @Value("${gateway.base-url}") String gatewayBaseUrl) {
        this.restTemplate = restTemplate;
        this.gatewayBaseUrl = gatewayBaseUrl;
    }

    public List<PatientDTO> getAllPatients() {
        String url = gatewayBaseUrl + "/patients";

        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

        ResponseEntity<List<PatientDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<PatientDTO>>() {}
        );

        return response.getBody() != null ? response.getBody() : Collections.emptyList();
    }

    public PatientDTO getPatientById(Integer id) {
        String url = gatewayBaseUrl + "/patients/" + id;

        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

        ResponseEntity<PatientDTO> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                PatientDTO.class
        );

        return response.getBody();
    }

    public void createPatient(PatientDTO patientDTO) {
        String url = gatewayBaseUrl + "/patients";

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PatientDTO> entity = new HttpEntity<>(patientDTO, headers);

        restTemplate.exchange(url, HttpMethod.POST, entity, PatientDTO.class);
    }

    public void updatePatient(Integer id, PatientDTO patientDTO) {
        String url = gatewayBaseUrl + "/patients/" + id;

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PatientDTO> entity = new HttpEntity<>(patientDTO, headers);

        restTemplate.exchange(url, HttpMethod.PUT, entity, PatientDTO.class);
    }

    public List<PatientDTO> searchPatients(String lastName, String firstName) {
        StringBuilder url = new StringBuilder(gatewayBaseUrl + "/patients/search?lastName=" + lastName);

        if (firstName != null && !firstName.isBlank()) {
            url.append("&firstName=").append(firstName);
        }

        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

        ResponseEntity<List<PatientDTO>> response = restTemplate.exchange(
                url.toString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<PatientDTO>>() {}
        );

        return response.getBody() != null ? response.getBody() : Collections.emptyList();
    }

    public void deletePatient(Integer id) {
        String url = gatewayBaseUrl + "/patients/" + id;

        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    private HttpHeaders createHeaders() {
        return new HttpHeaders();
    }
}