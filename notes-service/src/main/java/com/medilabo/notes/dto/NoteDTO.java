package com.medilabo.notes.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class NoteDTO {

    private String id;

    @NotNull(message = "Patient id is required")
    private Integer patientId;

    @NotBlank(message = "Note is required")
    private String note;

    private LocalDateTime createdAt;

    private Long version;

    public NoteDTO() {
    }

    public NoteDTO(String id, Integer patientId, String note, LocalDateTime createdAt) {
        this.id = id;
        this.patientId = patientId;
        this.note = note;
        this.createdAt = createdAt;
    }

    public String getId() {
        return id;
    }

    public Integer getPatientId() {
        return patientId;
    }

    public String getNote() {
        return note;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}