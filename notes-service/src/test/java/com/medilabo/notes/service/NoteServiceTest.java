package com.medilabo.notes.service;

import com.medilabo.notes.dto.NoteDTO;
import com.medilabo.notes.exception.NotFoundException;
import com.medilabo.notes.model.Note;
import com.medilabo.notes.repository.NoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.OptimisticLockingFailureException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NoteServiceTest {

    private NoteRepository noteRepository;
    private NoteService noteService;

    private Note routineNote;

    @BeforeEach
    void setUp() {
        noteRepository = mock(NoteRepository.class);
        noteService = new NoteService(noteRepository);

        routineNote = new Note(
                "note-lucas-b-01",
                101,
                "Routine consultation. No concerning symptom reported.",
                LocalDateTime.of(2026, 4, 21, 9, 15)
        );
        routineNote.setVersion(1L);
    }

    @Test
    void shouldReturnNotesByPatientId() {
        Note followUpNote = new Note(
                "note-emma-k-02",
                101,
                "Follow-up visit. Patient reports mild dizziness.",
                LocalDateTime.of(2026, 4, 22, 10, 30)
        );

        when(noteRepository.findByPatientId(101))
                .thenReturn(List.of(routineNote, followUpNote));

        List<NoteDTO> result = noteService.getNotesByPatientId(101);

        assertEquals(2, result.size());
        assertEquals("note-lucas-b-01", result.get(0).getId());
        assertEquals("Routine consultation. No concerning symptom reported.", result.get(0).getNote());
        assertEquals("note-emma-k-02", result.get(1).getId());
        assertEquals("Follow-up visit. Patient reports mild dizziness.", result.get(1).getNote());

        verify(noteRepository).findByPatientId(101);
    }

    @Test
    void shouldThrowExceptionWhenPatientIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            noteService.getNotesByPatientId(null);
        });

        verify(noteRepository, never()).findByPatientId(any());
    }

    @Test
    void shouldReturnNoteById() {
        when(noteRepository.findById("note-lucas-b-01"))
                .thenReturn(Optional.of(routineNote));

        NoteDTO result = noteService.getNoteById("note-lucas-b-01");

        assertEquals("note-lucas-b-01", result.getId());
        assertEquals(101, result.getPatientId());
        assertEquals("Routine consultation. No concerning symptom reported.", result.getNote());
    }

    @Test
    void shouldThrowNotFoundWhenNoteDoesNotExist() {
        when(noteRepository.findById("note-unknown-x"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            noteService.getNoteById("note-unknown-x");
        });
    }

    @Test
    void shouldCreateNote() {
        NoteDTO dto = new NoteDTO();
        dto.setPatientId(205);
        dto.setNote("Annual diabetes screening. Cholesterol monitoring required.");

        Note savedNote = new Note(
                "note-noah-rt-03",
                205,
                "Annual diabetes screening. Cholesterol monitoring required.",
                LocalDateTime.of(2026, 5, 3, 14, 45)
        );

        when(noteRepository.save(any(Note.class)))
                .thenReturn(savedNote);

        NoteDTO result = noteService.createNote(dto);

        assertEquals("note-noah-rt-03", result.getId());
        assertEquals(205, result.getPatientId());
        assertEquals("Annual diabetes screening. Cholesterol monitoring required.", result.getNote());
        assertNotNull(result.getCreatedAt());

        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void shouldUpdateNote() {
        NoteDTO dto = new NoteDTO();
        dto.setPatientId(309);
        dto.setNote("Updated consultation note. Smoking cessation discussed.");

        Note existingNote = new Note(
                "note-clara-z-04",
                309,
                "Initial consultation note.",
                LocalDateTime.of(2026, 5, 5, 8, 20)
        );

        Note updatedNote = new Note(
                "note-clara-z-04",
                309,
                "Updated consultation note. Smoking cessation discussed.",
                LocalDateTime.of(2026, 5, 5, 8, 20)
        );

        when(noteRepository.findById("note-clara-z-04"))
                .thenReturn(Optional.of(existingNote));

        when(noteRepository.save(any(Note.class)))
                .thenReturn(updatedNote);

        NoteDTO result = noteService.updateNote("note-clara-z-04", dto);

        assertEquals("note-clara-z-04", result.getId());
        assertEquals(309, result.getPatientId());
        assertEquals("Updated consultation note. Smoking cessation discussed.", result.getNote());

        verify(noteRepository).findById("note-clara-z-04");
        verify(noteRepository).save(any(Note.class));
    }

    @Test
    void shouldThrowNotFoundWhenUpdatingUnknownNote() {
        NoteDTO dto = new NoteDTO();
        dto.setPatientId(412);
        dto.setNote("Attempted update on missing note.");

        when(noteRepository.findById("note-missing-v"))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> {
            noteService.updateNote("note-missing-v", dto);
        });

        verify(noteRepository, never()).save(any(Note.class));
    }

    @Test
    void shouldDeleteNote() {
        when(noteRepository.existsById("note-hugo-m-05"))
                .thenReturn(true);

        noteService.deleteNote("note-hugo-m-05");

        verify(noteRepository).deleteById("note-hugo-m-05");
    }

    @Test
    void shouldThrowNotFoundWhenDeletingUnknownNote() {
        when(noteRepository.existsById("note-missing-p"))
                .thenReturn(false);

        assertThrows(NotFoundException.class, () -> {
            noteService.deleteNote("note-missing-p");
        });

        verify(noteRepository, never()).deleteById(anyString());
    }

    @Test
    void shouldThrowConflictWhenNoteIsUpdatedConcurrently() {
        NoteDTO dto = new NoteDTO();
        dto.setPatientId(518);
        dto.setNote("Updated note after another user modification.");

        Note existingNote = new Note(
                "note-alice-d-06",
                518,
                "Original note before concurrent update.",
                LocalDateTime.of(2026, 5, 8, 16, 10)
        );
        existingNote.setVersion(1L);

        when(noteRepository.findById("note-alice-d-06"))
                .thenReturn(Optional.of(existingNote));

        when(noteRepository.save(any(Note.class)))
                .thenThrow(new OptimisticLockingFailureException("Concurrent update"));

        assertThrows(OptimisticLockingFailureException.class, () -> {
            noteService.updateNote("note-alice-d-06", dto);
        });
    }
}