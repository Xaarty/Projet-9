package com.medilabo.patient.controller;

import com.medilabo.patient.dto.PatientDTO;
import com.medilabo.patient.model.Patient;
import com.medilabo.patient.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    public PatientController(PatientService patientService) {
        this.patientService = patientService;
    }


    //Récupération d'un patient par ID
    //Ajout de pagination. Limite le coût à long terme.
    @GetMapping
    public Page<PatientDTO> getAllPatients(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        return patientService.getAllPatients(page, size);
    }

    //Recherche de patient par nom voir prénom si renseigné
    @GetMapping("/search")
    public ResponseEntity<List<PatientDTO>> searchPatients(
            @RequestParam String lastName,
            @RequestParam(required = false) String firstName) {
        return ResponseEntity.ok(patientService.searchPatients(lastName, firstName));
    }

    //Création d'un patient
    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientDTO patientDTO) {
        return ResponseEntity.ok(patientService.createPatient(patientDTO));
    }

    //Modification de données patient
    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatient(@PathVariable Integer id,
                                                    @Valid @RequestBody PatientDTO patientDTO) {
        return ResponseEntity.ok(patientService.updatePatient(id, patientDTO));
    }

    //Suppression d'un patient
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Integer id) {
        patientService.deletePatient(id);
        return ResponseEntity.noContent().build();
    }
}