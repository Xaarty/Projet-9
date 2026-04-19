package com.medilabo.assessment.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AssessmentServiceTest {

    private final AssessmentService assessmentService =
            new AssessmentService(null, null);

    @Test
    void shouldReturnNoneWhenNoTrigger() {
        String result = assessmentService.determineAssessment(45, "M", 0);
        assertEquals("None", result);
    }

    @Test
    void shouldReturnBorderlineWhenAgeOver30AndTwoTriggers() {
        String result = assessmentService.determineAssessment(45, "M", 2);
        assertEquals("Borderline", result);
    }

    @Test
    void shouldReturnInDangerForManUnder30WithThreeTriggers() {
        String result = assessmentService.determineAssessment(25, "M", 3);
        assertEquals("InDanger", result);
    }

    @Test
    void shouldReturnEarlyOnsetForManUnder30WithFiveTriggers() {
        String result = assessmentService.determineAssessment(25, "M", 5);
        assertEquals("EarlyOnset", result);
    }

    @Test
    void shouldReturnInDangerForWomanUnder30WithFourTriggers() {
        String result = assessmentService.determineAssessment(25, "F", 4);
        assertEquals("InDanger", result);
    }

    @Test
    void shouldReturnEarlyOnsetForPatientOver30WithEightTriggers() {
        String result = assessmentService.determineAssessment(45, "M", 8);
        assertEquals("EarlyOnset", result);
    }
}