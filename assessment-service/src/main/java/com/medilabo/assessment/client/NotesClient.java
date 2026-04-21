package com.medilabo.assessment.client;

import com.medilabo.assessment.dto.NoteDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class NotesClient {

    private final RestTemplate restTemplate;

    public NotesClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Value("${notes-service.base-url}")
    private String notesServiceBaseUrl;

    public List<NoteDTO> getNotesByPatientId(Integer patientId) {
        return restTemplate.exchange(
                notesServiceBaseUrl + "/notes/patient/" + patientId,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<NoteDTO>>() {}
        ).getBody();
    }
}