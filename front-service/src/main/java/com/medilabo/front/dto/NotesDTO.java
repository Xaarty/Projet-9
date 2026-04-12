package com.medilabo.front.dto;

import java.time.LocalDateTime;

public class NotesDTO {

    private String id;
    private Integer patientId;
    private String note;
    private LocalDateTime createdAt;

    public NotesDTO() {
    }

    public NotesDTO(String id, Integer patientId, String note, LocalDateTime createdAt) {
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
}