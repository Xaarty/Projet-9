package com.medilabo.assessment.service;

import com.medilabo.assessment.client.NotesClient;
import com.medilabo.assessment.client.PatientClient;
import com.medilabo.assessment.dto.AssessmentResponseDTO;
import com.medilabo.assessment.dto.NoteDTO;
import com.medilabo.assessment.dto.PatientDTO;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
public class AssessmentService {

    private static final List<String> TRIGGERS = List.of(
            "hemoglobin a1c",
            "microalbumin",
            "height",
            "weight",
            "smoker",
            "abnormal",
            "cholesterol",
            "dizziness",
            "relapse",
            "reaction",
            "antibodies"
    );

    private final PatientClient patientClient;
    private final NotesClient notesClient;

    public AssessmentService(PatientClient patientClient, NotesClient notesClient) {
        this.patientClient = patientClient;
        this.notesClient = notesClient;
    }

    public AssessmentResponseDTO assessPatient(Integer patientId) {
        PatientDTO patient = patientClient.getPatientById(patientId);
        List<NoteDTO> notes = notesClient.getNotesByPatientId(patientId);

        int age = calculateAge(patient.getBirthDate());
        List<String> matchedTriggers = findMatchedTriggers(notes);
        int triggerCount = matchedTriggers.size();
        String assessment = determineAssessment(age, patient.getGender(), triggerCount);

        AssessmentResponseDTO response = new AssessmentResponseDTO();
        response.setPatientId(patient.getId());
        response.setFirstName(patient.getFirstName());
        response.setLastName(patient.getLastName());
        response.setAge(age);
        response.setTriggerCount(triggerCount);
        response.setAssessment(assessment);
        response.setMatchedTriggers(matchedTriggers);

        return response;
    }

    int calculateAge(LocalDate birthDate) {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    int countTriggers(List<NoteDTO> notes) {
        return findMatchedTriggers(notes).size();
    }

    List<String> findMatchedTriggers(List<NoteDTO> notes) {
        if (notes == null || notes.isEmpty()) {
            return List.of();
        }

        String combinedNotes = notes.stream()
                .map(NoteDTO::getNote)
                .filter(note -> note != null && !note.isBlank())
                .map(this::normalize)
                .reduce("", (a, b) -> a + " " + b);

        Set<String> matched = new LinkedHashSet<>();

        for (String trigger : TRIGGERS) {
            if (combinedNotes.contains(trigger)) {
                matched.add(trigger);
            }
        }

        return new ArrayList<>(matched);
    }

    String determineAssessment(int age, String gender, int triggerCount) {
        boolean isMale = "M".equalsIgnoreCase(gender);
        boolean isFemale = "F".equalsIgnoreCase(gender);

        if (age > 30) {
            if (triggerCount >= 8) {
                return "EarlyOnset";
            }
            if (triggerCount >= 6) {
                return "InDanger";
            }
            if (triggerCount >= 2) {
                return "Borderline";
            }
            return "None";
        }

        if (isMale) {
            if (triggerCount >= 5) {
                return "EarlyOnset";
            }
            if (triggerCount >= 3) {
                return "InDanger";
            }
            return "None";
        }

        if (isFemale) {
            if (triggerCount >= 7) {
                return "EarlyOnset";
            }
            if (triggerCount >= 4) {
                return "InDanger";
            }
            return "None";
        }

        return "None";
    }

    String normalize(String input) {
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        return normalized.replaceAll("\\p{M}", "").toLowerCase(Locale.ROOT);
    }
}