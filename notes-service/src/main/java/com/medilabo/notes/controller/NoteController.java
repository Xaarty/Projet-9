package com.medilabo.notes.controller;

import com.medilabo.notes.dto.NoteDTO;
import com.medilabo.notes.service.NoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notes")
public class NoteController {

    private final NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    //Récupération des notes liées a un patient
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<NoteDTO>> getNotesByPatientId(@PathVariable Integer patientId) {
        return ResponseEntity.ok(noteService.getNotesByPatientId(patientId));
    }

    //Récupération d'une note
    @GetMapping("/{id}")
    public ResponseEntity<NoteDTO> getNoteById(@PathVariable String id) {
        return ResponseEntity.ok(noteService.getNoteById(id));
    }

    //Création d'une note
    @PostMapping
    public ResponseEntity<NoteDTO> createNote(@Valid @RequestBody NoteDTO noteDTO) {
        return ResponseEntity.ok(noteService.createNote(noteDTO));
    }

    //Modification d'une note
    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable String id,
                                              @Valid @RequestBody NoteDTO noteDTO) {
        return ResponseEntity.ok(noteService.updateNote(id, noteDTO));
    }

    //Suppression d'une note
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable String id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }

}