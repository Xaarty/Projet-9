package com.medilabo.assessment.controller;

import com.medilabo.assessment.dto.AssessmentResponseDTO;
import com.medilabo.assessment.service.AssessmentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AssessmentController.class)
class AssessmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssessmentService assessmentService;

    @Test
    void shouldAssessPatient() throws Exception {
        AssessmentResponseDTO response = new AssessmentResponseDTO();
        response.setPatientId(1);
        response.setFirstName("Freddy");
        response.setLastName("RR");
        response.setAge(45);
        response.setTriggerCount(2);
        response.setAssessment("Borderline");
        response.setMatchedTriggers(List.of("fumeur", "cholesterol"));

        when(assessmentService.assessPatient(1)).thenReturn(response);

        mockMvc.perform(get("/assess/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.patientId").value(1))
                .andExpect(jsonPath("$.firstName").value("Freddy"))
                .andExpect(jsonPath("$.lastName").value("RR"))
                .andExpect(jsonPath("$.age").value(45))
                .andExpect(jsonPath("$.triggerCount").value(2))
                .andExpect(jsonPath("$.assessment").value("Borderline"))
                .andExpect(jsonPath("$.matchedTriggers[0]").value("fumeur"));
    }
}