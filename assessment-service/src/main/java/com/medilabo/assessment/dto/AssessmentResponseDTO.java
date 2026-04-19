package com.medilabo.assessment.dto;

import java.util.List;

public class AssessmentResponseDTO {
    private Integer patientId;
    private String firstName;
    private String lastName;
    private int age;
    private int triggerCount;
    private String assessment;
    private List<String> matchedTriggers;

    public AssessmentResponseDTO() {
    }

    public Integer getPatientId() {
        return patientId;
    }

    public void setPatientId(Integer patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getTriggerCount() {
        return triggerCount;
    }

    public void setTriggerCount(int triggerCount) {
        this.triggerCount = triggerCount;
    }

    public String getAssessment() {
        return assessment;
    }

    public void setAssessment(String assessment) {
        this.assessment = assessment;
    }

    public List<String> getMatchedTriggers() {
        return matchedTriggers;
    }

    public void setMatchedTriggers(List<String> matchedTriggers) {
        this.matchedTriggers = matchedTriggers;
    }
}