package com.medilabo.assessment.client;

import com.medilabo.assessment.dto.NoteDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class NotesClientTest {

    private RestTemplate restTemplate;
    private NotesClient notesClient;

    @BeforeEach
    void setUp() {
        restTemplate = mock(RestTemplate.class);
        notesClient = new NotesClient(restTemplate);
        ReflectionTestUtils.setField(notesClient, "notesServiceBaseUrl", "http://localhost:8083");
    }

    @Test
    void shouldReturnNotesByPatientId() {
        NoteDTO note = new NoteDTO();
        note.setId("note1");
        note.setPatientId(1);
        note.setNote("Test note");

        ResponseEntity<List<NoteDTO>> response =
                new ResponseEntity<>(List.of(note), HttpStatus.OK);

        when(restTemplate.exchange(
                eq("http://localhost:8083/notes/patient/1"),
                eq(HttpMethod.GET),
                isNull(),
                any(ParameterizedTypeReference.class)
        )).thenReturn(response);

        List<NoteDTO> result = notesClient.getNotesByPatientId(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test note", result.get(0).getNote());
    }
}