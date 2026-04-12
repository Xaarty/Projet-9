package com.medilabo.front.controller;

import com.medilabo.front.dto.PatientDTO;
import com.medilabo.front.service.PatientFrontService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PatientWebController.class)
@AutoConfigureMockMvc(addFilters = false)
class PatientWebControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientFrontService patientFrontService;

    @Test
    void shouldRedirectHomeToPatients() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients"));
    }

    @Test
    void shouldDisplayAllPatientsWhenNoSearchParameterIsProvided() throws Exception {
        PatientDTO patient = new PatientDTO(
                1, "John", "Doe", LocalDate.of(1990, 1, 1),
                "M", "1 Main Street", "0123456789"
        );

        when(patientFrontService.getAllPatients()).thenReturn(List.of(patient));

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(view().name("patients"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(model().attribute("patients", List.of(patient)));

        verify(patientFrontService).getAllPatients();
        verify(patientFrontService, never()).searchPatients(any(), any());
    }

    @Test
    void shouldSearchPatientsWhenLastNameIsProvided() throws Exception {
        PatientDTO patient = new PatientDTO(
                1, "John", "Doe", LocalDate.of(1990, 1, 1),
                "M", "1 Main Street", "0123456789"
        );

        when(patientFrontService.searchPatients("Doe", "John")).thenReturn(List.of(patient));

        mockMvc.perform(get("/patients")
                        .param("lastName", "Doe")
                        .param("firstName", "John"))
                .andExpect(status().isOk())
                .andExpect(view().name("patients"))
                .andExpect(model().attributeExists("patients"))
                .andExpect(model().attribute("lastName", "Doe"))
                .andExpect(model().attribute("firstName", "John"));

        verify(patientFrontService).searchPatients("Doe", "John");
        verify(patientFrontService, never()).getAllPatients();
    }

    @Test
    void shouldDisplayErrorAndAllPatientsWhenLastNameIsBlank() throws Exception {
        when(patientFrontService.getAllPatients()).thenReturn(List.of());

        mockMvc.perform(get("/patients")
                        .param("lastName", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("patients"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Last name is required for search"))
                .andExpect(model().attributeExists("patients"));

        verify(patientFrontService).getAllPatients();
        verify(patientFrontService, never()).searchPatients(any(), any());
    }

    @Test
    void shouldDisplayAddPatientForm() throws Exception {
        mockMvc.perform(get("/patients/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient-form"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attribute("formAction", "/patients/add"))
                .andExpect(model().attribute("formTitle", "Add Patient"));
    }

    @Test
    void shouldCreatePatientWhenFormIsValid() throws Exception {
        mockMvc.perform(post("/patients/add")
                        .param("firstName", "John")
                        .param("lastName", "Doe")
                        .param("birthDate", "1990-01-01")
                        .param("gender", "M")
                        .param("address", "1 Main Street")
                        .param("phone", "0123456789"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients"));

        verify(patientFrontService).createPatient(any(PatientDTO.class));
    }

    @Test
    void shouldReturnAddFormWhenCreatePatientFormIsInvalid() throws Exception {
        mockMvc.perform(post("/patients/add")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("birthDate", "")
                        .param("gender", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("patient-form"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attribute("formAction", "/patients/add"))
                .andExpect(model().attribute("formTitle", "Add Patient"));

        verify(patientFrontService, never()).createPatient(any(PatientDTO.class));
    }

    @Test
    void shouldDisplayEditPatientForm() throws Exception {
        PatientDTO patient = new PatientDTO(
                1, "John", "Doe", LocalDate.of(1990, 1, 1),
                "M", "1 Main Street", "0123456789"
        );

        when(patientFrontService.getPatientById(1)).thenReturn(patient);

        mockMvc.perform(get("/patients/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient-form"))
                .andExpect(model().attributeExists("patient"))
                .andExpect(model().attribute("formAction", "/patients/edit/1"))
                .andExpect(model().attribute("formTitle", "Edit Patient"));

        verify(patientFrontService).getPatientById(1);
    }

    @Test
    void shouldUpdatePatientWhenFormIsValid() throws Exception {
        mockMvc.perform(post("/patients/edit/1")
                        .param("firstName", "Jane")
                        .param("lastName", "Doe")
                        .param("birthDate", "1992-02-02")
                        .param("gender", "F")
                        .param("address", "2 New Street")
                        .param("phone", "0987654321"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients"));

        verify(patientFrontService).updatePatient(eq(1), any(PatientDTO.class));
    }

    @Test
    void shouldReturnEditFormWhenUpdatePatientFormIsInvalid() throws Exception {
        mockMvc.perform(post("/patients/edit/1")
                        .param("firstName", "")
                        .param("lastName", "")
                        .param("birthDate", "")
                        .param("gender", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("patient-form"))
                .andExpect(model().attribute("formAction", "/patients/edit/1"))
                .andExpect(model().attribute("formTitle", "Edit Patient"));

        verify(patientFrontService, never()).updatePatient(eq(1), any(PatientDTO.class));
    }

    @Test
    void shouldDeletePatient() throws Exception {
        mockMvc.perform(post("/patients/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients"));

        verify(patientFrontService).deletePatient(1);
    }
}