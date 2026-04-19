package com.medilabo.assessment.controller;

import com.medilabo.assessment.dto.AssessmentResponseDTO;
import com.medilabo.assessment.service.AssessmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AssessmentController {

    private final AssessmentService assessmentService;

    public AssessmentController(AssessmentService assessmentService) {
        this.assessmentService = assessmentService;
    }

    @GetMapping("/assess/{patientId}")
    public AssessmentResponseDTO assessPatient(@PathVariable Integer patientId) {
        return assessmentService.assessPatient(patientId);
    }
}