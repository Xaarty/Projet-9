package com.medilabo.notes.service;

import com.medilabo.notes.dto.NoteDTO;
import com.medilabo.notes.exception.NotFoundException;
import com.medilabo.notes.model.Note;
import com.medilabo.notes.repository.NoteRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NoteService {

    private final NoteRepository noteRepository;

    public NoteService(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    //Récupéreation des notes d'un patient
    public List<NoteDTO> getNotesByPatientId(Integer patientId) {
        if (patientId == null) {
            throw new IllegalArgumentException("Patient id is required");
        }

        return noteRepository.findByPatientId(patientId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    //Récupération d'une note
    public NoteDTO getNoteById(String id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Note not found with id: " + id));

        return convertToDTO(note);
    }

    //Création d'une note
    public NoteDTO createNote(NoteDTO dto) {
        Note note = new Note();
        note.setPatientId(dto.getPatientId());
        note.setNote(dto.getNote());
        note.setCreatedAt(LocalDateTime.now());

        Note savedNote = noteRepository.save(note);
        return convertToDTO(savedNote);
    }

    //Modification d'une note
    public void deleteNote(String id) {
        if (!noteRepository.existsById(id)) {
            throw new NotFoundException("Note not found with id: " + id);
        }

        noteRepository.deleteById(id);
    }

    //Suppression d'une note
    public NoteDTO updateNote(String id, NoteDTO dto) {
        Note existingNote = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Note not found with id: " + id));

        existingNote.setPatientId(dto.getPatientId());
        existingNote.setNote(dto.getNote());

        Note updatedNote = noteRepository.save(existingNote);
        return convertToDTO(updatedNote);
    }

    // Convertit l’entité MongoDB pour la manipuler
    private NoteDTO convertToDTO(Note note) {
        NoteDTO dto = new NoteDTO(
                note.getId(),
                note.getPatientId(),
                note.getNote(),
                note.getCreatedAt()
        );

        dto.setVersion(note.getVersion());

        return dto;
    }
}