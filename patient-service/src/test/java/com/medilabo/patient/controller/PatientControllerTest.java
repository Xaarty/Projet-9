package com.medilabo.patient.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medilabo.patient.dto.PatientDTO;
import com.medilabo.patient.exception.GlobalExceptionHandler;
import com.medilabo.patient.exception.NotFoundException;
import com.medilabo.patient.service.PatientService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
                101,
                "Lucas",
                "B",
                LocalDate.of(1988, 5, 12),
                "M",
                "10 Oak Street",
                "0102030405"
        );

        Page<PatientDTO> page = new PageImpl<>(List.of(patient));

        when(patientService.getAllPatients(0, 20)).thenReturn(page);

        mockMvc.perform(get("/patients")
                        .param("page", "0")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(101))
                .andExpect(jsonPath("$.content[0].firstName").value("Lucas"))
                .andExpect(jsonPath("$.content[0].lastName").value("B"));
    }

    @Test
    void shouldReturnPatientByIdWhenPatientExists() throws Exception {
        PatientDTO patient = new PatientDTO(
                205,
                "Emma",
                "K",
                LocalDate.of(1995, 8, 3),
                "F",
                "22 Pine Avenue",
                "0607080910"
        );

        when(patientService.getPatientById(205)).thenReturn(patient);

        mockMvc.perform(get("/patients/205"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(205))
                .andExpect(jsonPath("$.firstName").value("Emma"))
                .andExpect(jsonPath("$.lastName").value("K"));
    }

    @Test
    void shouldReturn404WhenGettingPatientByIdAndPatientDoesNotExist() throws Exception {
        when(patientService.getPatientById(309))
                .thenThrow(new NotFoundException("Patient not found with id: 309"));

        mockMvc.perform(get("/patients/309"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found with id: 309"))
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void shouldSearchPatients() throws Exception {
        PatientDTO patient = new PatientDTO(
                412,
                "Lea",
                "V",
                LocalDate.of(2001, 3, 14),
                "F",
                "14 Lake Street",
                "0708091011"
        );

        when(patientService.searchPatients("V", "Lea")).thenReturn(List.of(patient));

        mockMvc.perform(get("/patients/search")
                        .param("lastName", "V")
                        .param("firstName", "Lea"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Lea"))
                .andExpect(jsonPath("$[0].lastName").value("V"));
    }

    @Test
    void shouldCreatePatient() throws Exception {
        PatientDTO patient = new PatientDTO(
                518,
                "Hugo",
                "M",
                LocalDate.of(1983, 6, 9),
                "M",
                "18 Forest Lane",
                "0203040506"
        );

        when(patientService.createPatient(any(PatientDTO.class))).thenReturn(patient);

        mockMvc.perform(post("/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(patient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(518))
                .andExpect(jsonPath("$.firstName").value("Hugo"))
                .andExpect(jsonPath("$.lastName").value("M"));
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
                623,
                "Clara",
                "Z",
                LocalDate.of(1992, 12, 1),
                "F",
                "7 Hill Road",
                "0304050607"
        );

        when(patientService.updatePatient(eq(623), any(PatientDTO.class))).thenReturn(updatedPatient);

        mockMvc.perform(put("/patients/623")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedPatient)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Clara"))
                .andExpect(jsonPath("$.lastName").value("Z"))
                .andExpect(jsonPath("$.gender").value("F"));
    }

    @Test
    void shouldDeletePatient() throws Exception {
        doNothing().when(patientService).deletePatient(734);

        mockMvc.perform(delete("/patients/734"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenDeletingPatientAndPatientDoesNotExist() throws Exception {
        doThrow(new NotFoundException("Patient not found with id: 845"))
                .when(patientService).deletePatient(845);

        mockMvc.perform(delete("/patients/845"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Patient not found with id: 845"))
                .andExpect(jsonPath("$.status").value(404));
    }
}