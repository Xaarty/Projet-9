package com.medilabo.front.controller;

import com.medilabo.front.dto.AssessmentDTO;
import com.medilabo.front.dto.NotesDTO;
import com.medilabo.front.dto.PatientDTO;
import com.medilabo.front.dto.PatientPageDTO;
import com.medilabo.front.service.PatientFrontService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
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
                101, "Sabrina", "R", LocalDate.of(1990, 1, 1),
                "F", "Rue 1", "0123456789"
        );

        PatientPageDTO patientPage = new PatientPageDTO();
        patientPage.setContent(List.of(patient));
        patientPage.setNumber(0);
        patientPage.setSize(20);
        patientPage.setTotalElements(1);
        patientPage.setTotalPages(1);

        when(patientFrontService.getAllPatients(0, 20)).thenReturn(patientPage);

        mockMvc.perform(get("/patients"))
                .andExpect(status().isOk())
                .andExpect(view().name("patients"))
                .andExpect(model().attribute("patients", List.of(patient)))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("pageSize", 20))
                .andExpect(model().attribute("hasPrevious", false))
                .andExpect(model().attribute("hasNext", false));

        verify(patientFrontService).getAllPatients(0, 20);
        verify(patientFrontService, never()).searchPatients(any(), any(), anyInt(), anyInt());
    }

    @Test
    void shouldSearchPatientsWhenLastNameIsProvided() throws Exception {
        PatientDTO patient = new PatientDTO(
                205, "Lucas", "B", LocalDate.of(1988, 5, 12),
                "M", "10 Oak Street", "0102030405"
        );

        PatientPageDTO patientPage = new PatientPageDTO();
        patientPage.setContent(List.of(patient));
        patientPage.setNumber(0);
        patientPage.setSize(20);
        patientPage.setTotalElements(1);
        patientPage.setTotalPages(1);

        when(patientFrontService.searchPatients("B", "Lucas", 0, 20)).thenReturn(patientPage);

        mockMvc.perform(get("/patients")
                        .param("lastName", "B")
                        .param("firstName", "Lucas"))
                .andExpect(status().isOk())
                .andExpect(view().name("patients"))
                .andExpect(model().attribute("patients", List.of(patient)))
                .andExpect(model().attribute("lastName", "B"))
                .andExpect(model().attribute("firstName", "Lucas"));

        verify(patientFrontService).searchPatients("B", "Lucas", 0, 20);
        verify(patientFrontService, never()).getAllPatients(anyInt(), anyInt());
    }

    @Test
    void shouldDisplayErrorAndAllPatientsWhenLastNameIsBlank() throws Exception {
        PatientPageDTO patientPage = new PatientPageDTO();
        patientPage.setContent(List.of());
        patientPage.setNumber(0);
        patientPage.setSize(20);
        patientPage.setTotalElements(0);
        patientPage.setTotalPages(0);

        when(patientFrontService.getAllPatients(0, 20)).thenReturn(patientPage);

        mockMvc.perform(get("/patients")
                        .param("lastName", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("patients"))
                .andExpect(model().attribute("error", "Last name is required for search"))
                .andExpect(model().attribute("patients", List.of()))
                .andExpect(model().attribute("currentPage", 0))
                .andExpect(model().attribute("pageSize", 20));

        verify(patientFrontService).getAllPatients(0, 20);
        verify(patientFrontService, never()).searchPatients(any(), any(), anyInt(), anyInt());
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
                        .param("firstName", "Emma")
                        .param("lastName", "K")
                        .param("birthDate", "1995-08-03")
                        .param("gender", "F")
                        .param("address", "22 Pine Avenue")
                        .param("phone", "0607080910"))
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
                309, "Noah", "RT", LocalDate.of(1979, 11, 20),
                "M", "5 River Road", "0111222333"
        );

        when(patientFrontService.getPatientById(309)).thenReturn(patient);

        mockMvc.perform(get("/patients/edit/309"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient-form"))
                .andExpect(model().attribute("patient", patient))
                .andExpect(model().attribute("formAction", "/patients/edit/309"))
                .andExpect(model().attribute("formTitle", "Edit Patient"));

        verify(patientFrontService).getPatientById(309);
    }

    @Test
    void shouldUpdatePatientWhenFormIsValid() throws Exception {
        mockMvc.perform(post("/patients/edit/412")
                        .param("id", "412")
                        .param("firstName", "Lea")
                        .param("lastName", "V")
                        .param("birthDate", "2001-03-14")
                        .param("gender", "F")
                        .param("address", "14 Lake Street")
                        .param("phone", "0708091011")
                        .param("version", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients"));

        verify(patientFrontService).updatePatient(eq(412), any(PatientDTO.class));
    }

    @Test
    void shouldDeletePatient() throws Exception {
        mockMvc.perform(post("/patients/delete/623"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients"));

        verify(patientFrontService).deletePatient(623);
    }

    @Test
    void shouldDisplayPatientHistory() throws Exception {
        PatientDTO patient = new PatientDTO(
                734, "Clara", "Z", LocalDate.of(1992, 12, 1),
                "F", "7 Hill Road", "0304050607"
        );

        NotesDTO note = new NotesDTO();
        note.setId("note-clara-z-01");
        note.setPatientId(734);
        note.setNote("Patient note");

        AssessmentDTO assessment = new AssessmentDTO();
        assessment.setPatientId(734);
        assessment.setAssessment("Borderline");

        when(patientFrontService.getPatientById(734)).thenReturn(patient);
        when(patientFrontService.getNotesByPatientId(734)).thenReturn(List.of(note));
        when(patientFrontService.getAssessmentByPatientId(734)).thenReturn(assessment);

        mockMvc.perform(get("/patients/734/history"))
                .andExpect(status().isOk())
                .andExpect(view().name("patient-history"))
                .andExpect(model().attribute("patient", patient))
                .andExpect(model().attribute("notes", List.of(note)))
                .andExpect(model().attribute("assessment", assessment));

        verify(patientFrontService).getPatientById(734);
        verify(patientFrontService).getNotesByPatientId(734);
        verify(patientFrontService).getAssessmentByPatientId(734);
    }

    @Test
    void shouldAddPatientNote() throws Exception {
        mockMvc.perform(post("/patients/845/history")
                        .param("note", "New note"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients/845/history"));

        verify(patientFrontService).createNote(any(NotesDTO.class));
    }

    @Test
    void shouldDisplayEditNoteForm() throws Exception {
        NotesDTO note = new NotesDTO();
        note.setId("note-hugo-m-01");
        note.setPatientId(956);
        note.setNote("Existing note");

        when(patientFrontService.getNoteById("note-hugo-m-01")).thenReturn(note);

        mockMvc.perform(get("/patients/956/history/edit-note/note-hugo-m-01"))
                .andExpect(status().isOk())
                .andExpect(view().name("note-form"))
                .andExpect(model().attribute("noteItem", note))
                .andExpect(model().attribute("patientId", 956));

        verify(patientFrontService).getNoteById("note-hugo-m-01");
    }

    @Test
    void shouldUpdatePatientNote() throws Exception {
        NotesDTO note = new NotesDTO();
        note.setId("note-alice-d-01");
        note.setPatientId(106);
        note.setNote("Old note");

        when(patientFrontService.getNoteById("note-alice-d-01")).thenReturn(note);

        mockMvc.perform(post("/patients/106/history/edit-note/note-alice-d-01")
                        .param("note", "Updated note"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/patients/106/history"));

        verify(patientFrontService).updateNote(eq("note-alice-d-01"), any(NotesDTO.class));
    }
}