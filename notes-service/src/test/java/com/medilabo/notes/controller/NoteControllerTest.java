package com.medilabo.notes.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.notes.dto.NoteDTO;
import com.medilabo.notes.service.NoteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import com.medilabo.notes.exception.GlobalExceptionHandler;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NoteController.class)
@Import(GlobalExceptionHandler.class)
class NoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NoteService noteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldReturnNotesByPatientId() throws Exception {
        NoteDTO note = new NoteDTO("note1", 1, "Test note", LocalDateTime.now());

        when(noteService.getNotesByPatientId(1)).thenReturn(List.of(note));

        mockMvc.perform(get("/notes/patient/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("note1"))
                .andExpect(jsonPath("$[0].patientId").value(1))
                .andExpect(jsonPath("$[0].note").value("Test note"));

        verify(noteService).getNotesByPatientId(1);
    }

    @Test
    void shouldReturnNoteById() throws Exception {
        NoteDTO note = new NoteDTO("note1", 1, "Test note", LocalDateTime.now());

        when(noteService.getNoteById("note1")).thenReturn(note);

        mockMvc.perform(get("/notes/note1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("note1"))
                .andExpect(jsonPath("$.note").value("Test note"));

        verify(noteService).getNoteById("note1");
    }

    @Test
    void shouldCreateNote() throws Exception {
        NoteDTO input = new NoteDTO();
        input.setPatientId(1);
        input.setNote("New note");

        NoteDTO output = new NoteDTO("note1", 1, "New note", LocalDateTime.now());

        when(noteService.createNote(any(NoteDTO.class))).thenReturn(output);

        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("note1"))
                .andExpect(jsonPath("$.note").value("New note"));

        verify(noteService).createNote(any(NoteDTO.class));
    }

        @Test
    void shouldReturnBadRequestWhenCreateNoteIsInvalid() throws Exception {
        NoteDTO input = new NoteDTO();
        input.setPatientId(null);
        input.setNote("");

        mockMvc.perform(post("/notes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"));
    }

    @Test
    void shouldUpdateNote() throws Exception {
        NoteDTO input = new NoteDTO();
        input.setPatientId(1);
        input.setNote("Updated note");

        NoteDTO output = new NoteDTO("note1", 1, "Updated note", LocalDateTime.now());

        when(noteService.updateNote(eq("note1"), any(NoteDTO.class))).thenReturn(output);

        mockMvc.perform(put("/notes/note1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("note1"))
                .andExpect(jsonPath("$.note").value("Updated note"));

        verify(noteService).updateNote(eq("note1"), any(NoteDTO.class));
    }

    @Test
    void shouldDeleteNote() throws Exception {
        mockMvc.perform(delete("/notes/note1"))
                .andExpect(status().isNoContent());

        verify(noteService).deleteNote("note1");
    }
}