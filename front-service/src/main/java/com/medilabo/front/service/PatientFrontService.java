package com.medilabo.front.service;

import com.medilabo.front.dto.AssessmentDTO;
import com.medilabo.front.dto.NotesDTO;
import com.medilabo.front.dto.PatientDTO;
import com.medilabo.front.dto.PatientPageDTO;
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
    // URL de la gateway
    private final String gatewayBaseUrl;

    public PatientFrontService(RestTemplate restTemplate,
                               @Value("${gateway.base-url}") String gatewayBaseUrl) {
        this.restTemplate = restTemplate;
        this.gatewayBaseUrl = gatewayBaseUrl;
    }

    // Retourne la liste des patients avec pagination
    public PatientPageDTO getAllPatients(int page, int size) {
        String url = gatewayBaseUrl + "/patients?page=" + page + "&size=" + size;

        PatientPageDTO response = restTemplate.getForObject(
                url,
                PatientPageDTO.class
        );

        return response != null ? response : new PatientPageDTO();
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

    // Création d’un patient
    public void createPatient(PatientDTO patientDTO) {
        String url = gatewayBaseUrl + "/patients";

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PatientDTO> entity = new HttpEntity<>(patientDTO, headers);

        restTemplate.exchange(url, HttpMethod.POST, entity, PatientDTO.class);
    }

    //Modification d'un patient
    public void updatePatient(Integer id, PatientDTO patientDTO) {
        String url = gatewayBaseUrl + "/patients/" + id;

        HttpHeaders headers = createHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<PatientDTO> entity = new HttpEntity<>(patientDTO, headers);

        restTemplate.exchange(url, HttpMethod.PUT, entity, PatientDTO.class);
    }

    // Cherche un patient via nom de famille et prénom si renseigné, avec pagination
    public PatientPageDTO searchPatients(String lastName, String firstName, int page, int size) {
        StringBuilder url = new StringBuilder(
                gatewayBaseUrl + "/patients/search?lastName=" + lastName
                        + "&page=" + page
                        + "&size=" + size
        );

        if (firstName != null && !firstName.isBlank()) {
            url.append("&firstName=").append(firstName);
        }

        PatientPageDTO response = restTemplate.getForObject(
                url.toString(),
                PatientPageDTO.class
        );

        return response != null ? response : new PatientPageDTO();
    }

    //Suppression d'un patient
    public void deletePatient(Integer id) {
        String url = gatewayBaseUrl + "/patients/" + id;

        HttpEntity<Void> entity = new HttpEntity<>(createHeaders());

        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }

    //Header pour requêtes https
    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    //Recupération des notes via l'ID patient
    public List<NotesDTO> getNotesByPatientId(Integer patientId) {
        String url = gatewayBaseUrl + "/notes/patient/" + patientId;

        ResponseEntity<List<NotesDTO>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<NotesDTO>>() {}
        );

        return response.getBody() != null ? response.getBody() : Collections.emptyList();
    }

    //Création d'une note
    public void createNote(NotesDTO noteDTO) {
        String url = gatewayBaseUrl + "/notes";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<NotesDTO> entity = new HttpEntity<>(noteDTO, headers);

        restTemplate.exchange(url, HttpMethod.POST, entity, NotesDTO.class);
    }

    //Récupération de l'assessment du patient (risque diabete)
    public AssessmentDTO getAssessmentByPatientId(Integer id) {
        return restTemplate.getForObject(
                gatewayBaseUrl + "/assess/" + id,
                AssessmentDTO.class
        );
    }

    //Récupérer les notes via ID patient
    public NotesDTO getNoteById(String id) {
        return restTemplate.getForObject(
                gatewayBaseUrl + "/notes/" + id,
                NotesDTO.class
        );
    }

    //Modification d'une note patient
    public NotesDTO updateNote(String id, NotesDTO noteDTO) {
        restTemplate.put(
                gatewayBaseUrl + "/notes/" + id,
                noteDTO
        );
        return getNoteById(id);
    }
}