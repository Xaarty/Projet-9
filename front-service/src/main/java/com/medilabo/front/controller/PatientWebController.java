package com.medilabo.front.controller;

import com.medilabo.front.dto.AssessmentDTO;
import com.medilabo.front.dto.NotesDTO;
import com.medilabo.front.dto.PatientDTO;
import com.medilabo.front.dto.PatientPageDTO;
import com.medilabo.front.service.PatientFrontService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class PatientWebController {

    private final PatientFrontService patientFrontService;

    public PatientWebController(PatientFrontService patientFrontService) {
        this.patientFrontService = patientFrontService;
    }

    // Redirection vers la page principale
    @GetMapping("/")
    public String home() {
        return "redirect:/patients";
    }

    @GetMapping("/patients")
    public String getPatients(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String firstName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Model model) {

        PatientPageDTO patientPage;

        if (lastName != null) {
            if (lastName.isBlank()) {
                model.addAttribute("error", "Last name is required for search");
                patientPage = patientFrontService.getAllPatients(page, size);
            } else {
                patientPage = patientFrontService.searchPatients(lastName, firstName, page, size);
            }
        } else {
            patientPage = patientFrontService.getAllPatients(page, size);
        }

        model.addAttribute("patients", patientPage.getContent());
        model.addAttribute("currentPage", patientPage.getNumber());
        model.addAttribute("pageSize", patientPage.getSize());
        model.addAttribute("hasPrevious", patientPage.isHasPrevious());
        model.addAttribute("hasNext", patientPage.isHasNext());

        model.addAttribute("lastName", lastName);
        model.addAttribute("firstName", firstName);

        return "patients";
    }

    // Formulaire d’ajout de patients
    @GetMapping("/patients/add")
    public String showAddForm(Model model) {
        model.addAttribute("patient", new PatientDTO());
        model.addAttribute("formAction", "/patients/add");
        model.addAttribute("formTitle", "Add Patient");
        return "patient-form";
    }

    // Création du patient
    @PostMapping("/patients/add")
    public String addPatient(@Valid @ModelAttribute("patient") PatientDTO patientDTO,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/patients/add");
            model.addAttribute("formTitle", "Add Patient");
            return "patient-form";
        }

        patientFrontService.createPatient(patientDTO);
        return "redirect:/patients";
    }

    // Charge le patient existant et affiche le formulaire de modification
    @GetMapping("/patients/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        PatientDTO patient = patientFrontService.getPatientById(id);
        model.addAttribute("patient", patient);
        model.addAttribute("formAction", "/patients/edit/" + id);
        model.addAttribute("formTitle", "Edit Patient");
        return "patient-form";
    }

    // Mise à jour du patient
    @PostMapping("/patients/edit/{id}")
    public String updatePatient(@PathVariable Integer id,
                                @Valid @ModelAttribute("patient") PatientDTO patientDTO,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/patients/edit/" + id);
            model.addAttribute("formTitle", "Edit Patient");
            return "patient-form";
        }

        patientFrontService.updatePatient(id, patientDTO);
        return "redirect:/patients";
    }

    // Suppression du patient
    @PostMapping("/patients/delete/{id}")
    public String deletePatient(@PathVariable Integer id) {
        patientFrontService.deletePatient(id);
        return "redirect:/patients";
    }

    // Page d'informations du patient, des notes et son évaluation de risque de diabete
    @GetMapping("/patients/{id}/history")
    public String showPatientHistory(@PathVariable Integer id, Model model) {
        PatientDTO patient = patientFrontService.getPatientById(id);
        List<NotesDTO> notes = patientFrontService.getNotesByPatientId(id);
        AssessmentDTO assessment = patientFrontService.getAssessmentByPatientId(id);

        model.addAttribute("patient", patient);
        model.addAttribute("notes", notes);
        model.addAttribute("assessment", assessment);

        return "patient-history";
    }

    // Création d’une note
    @PostMapping("/patients/{id}/history")
    public String addPatientNote(@PathVariable Integer id,
                                 @RequestParam String note) {
        NotesDTO noteDTO = new NotesDTO();
        noteDTO.setPatientId(id);
        noteDTO.setNote(note);

        patientFrontService.createNote(noteDTO);

        return "redirect:/patients/" + id + "/history";
    }

    // Charge une note et le formulaire d’édition
    @GetMapping("/patients/{patientId}/history/edit-note/{noteId}")
    public String showEditNoteForm(@PathVariable Integer patientId,
                                   @PathVariable String noteId,
                                   Model model) {
        NotesDTO note = patientFrontService.getNoteById(noteId);

        model.addAttribute("noteItem", note);
        model.addAttribute("patientId", patientId);

        return "note-form";
    }

    // Modification de note de patient
    @PostMapping("/patients/{patientId}/history/edit-note/{noteId}")
    public String updatePatientNote(@PathVariable Integer patientId,
                                    @PathVariable String noteId,
                                    @RequestParam String note) {
        NotesDTO noteDTO = patientFrontService.getNoteById(noteId);
        noteDTO.setNote(note);

        patientFrontService.updateNote(noteId, noteDTO);

        return "redirect:/patients/" + patientId + "/history";
    }
}