package com.medilabo.front.controller;

import com.medilabo.front.dto.PatientDTO;
import com.medilabo.front.service.PatientFrontService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class PatientWebController {

    private final PatientFrontService patientFrontService;

    public PatientWebController(PatientFrontService patientFrontService) {
        this.patientFrontService = patientFrontService;
    }

    @GetMapping("/")
    public String home() {
        return "redirect:/patients";
    }

    @GetMapping("/patients")
    public String getPatients(
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) String firstName,
            Model model) {

        if (lastName != null && !lastName.isBlank()) {
            model.addAttribute("patients", patientFrontService.searchPatients(lastName, firstName));
        } else {
            model.addAttribute("error", "Last name is required for search");
            model.addAttribute("patients", patientFrontService.getAllPatients());
        }

        model.addAttribute("lastName", lastName);
        model.addAttribute("firstName", firstName);

        return "patients";
    }

    @GetMapping("/patients/add")
    public String showAddForm(Model model) {
        model.addAttribute("patient", new PatientDTO());
        model.addAttribute("formAction", "/patients/add");
        model.addAttribute("formTitle", "Add Patient");
        return "patient-form";
    }

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

    @GetMapping("/patients/edit/{id}")
    public String showEditForm(@PathVariable Integer id, Model model) {
        PatientDTO patient = patientFrontService.getPatientById(id);
        model.addAttribute("patient", patient);
        model.addAttribute("formAction", "/patients/edit/" + id);
        model.addAttribute("formTitle", "Edit Patient");
        return "patient-form";
    }

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

    @PostMapping("/patients/delete/{id}")
    public String deletePatient(@PathVariable Integer id) {
        patientFrontService.deletePatient(id);
        return "redirect:/patients";
    }
}