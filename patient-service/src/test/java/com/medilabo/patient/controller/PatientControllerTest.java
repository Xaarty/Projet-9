package com.medilabo.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.patient.dto.PatientDTO;
import com.medilabo.patient.exception.GlobalExceptionHandler;
import com.medilabo.patient.exception.NotFoundException;
import com.medilabo.patient.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientController.class)
@Import(GlobalExceptionHandler.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PatientService patientService;

    @Test
    void shouldReturnAllPatients() throws Exception {
        PatientDTO patient = new PatientDTO(
                1,
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "M",
                "1 Main Street",
                "0123456789"
        );

        Page<PatientDTO> page = new PageImpl<>(List.of(patient));

        when(patientService.getAllPatients(0, 20)).thenReturn(page);

        mockMvc.perform(get("/patients")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void shouldReturnPatientByIdWhenPatientExists() throws Exception {
        PatientDTO patient = new PatientDTO(
                1,
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "M",
                "1 Main Street",
                "0123456789"
        );

        when(patientService.getPatientById(1)).thenReturn(patient);

        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    void shouldReturn404WhenGettingPatientByIdAndPatientDoesNotExist() throws Exception {
        when(patientService.getPatientById(1))
                .thenThrow(new NotFoundException("Patient not found with id: 1"));

        mockMvc.perform(get("/patients/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found with id: 1"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldSearchPatients() throws Exception {
        PatientDTO patient = new PatientDTO(
                1,
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "M",
                "1 Main Street",
                "0123456789"
        );

        when(patientService.searchPatients("Doe", "John")).thenReturn(List.of(patient));

        mockMvc.perform(get("/patients/search")
                        .param("lastName", "Doe")
                        .param("firstName", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"));
    }

    @Test
    void shouldCreatePatient() throws Exception {
        PatientDTO patient = new PatientDTO(
                1,
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "M",
                "1 Main Street",
                "0123456789"
        );

        when(patientService.createPatient(any(PatientDTO.class))).thenReturn(patient);

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    void shouldReturn400WhenCreatingPatientWithInvalidBody() throws Exception {
        PatientDTO invalidPatient = new PatientDTO(
                null,
                "",
                "",
                null,
                "",
                "1 Main Street",
                "0123456789"
        );

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPatient)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation error"))
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.errors.firstName").exists())
                .andExpect(jsonPath("$.errors.lastName").exists())
                .andExpect(jsonPath("$.errors.birthDate").exists())
                .andExpect(jsonPath("$.errors.gender").exists());
    }

    @Test
    void shouldUpdatePatient() throws Exception {
        PatientDTO updatedPatient = new PatientDTO(
                1,
                "Jane",
                "Doe",
                LocalDate.of(1992, 2, 2),
                "F",
                "2 New Street",
                "0987654321"
        );

        when(patientService.updatePatient(eq(1), any(PatientDTO.class))).thenReturn(updatedPatient);

        mockMvc.perform(put("/patients/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jane"))
                .andExpect(jsonPath("$.gender").value("F"));
    }

    @Test
    void shouldDeletePatient() throws Exception {
        doNothing().when(patientService).deletePatient(1);

        mockMvc.perform(delete("/patients/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenDeletingPatientAndPatientDoesNotExist() throws Exception {
        doThrow(new NotFoundException("Patient not found with id: 1"))
                .when(patientService).deletePatient(1);

        mockMvc.perform(delete("/patients/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found with id: 1"))
                .andExpect(jsonPath("$.status").value(404));
    }
}