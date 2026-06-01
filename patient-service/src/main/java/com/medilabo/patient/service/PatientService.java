package com.medilabo.patient.service;

import com.medilabo.patient.dto.PatientDTO;
import com.medilabo.patient.exception.NotFoundException;
import com.medilabo.patient.model.Patient;
import com.medilabo.patient.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    //Récupération de tout les patients
    public Page<PatientDTO> getAllPatients(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return patientRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    //Récupération d'un patient par ID
    public PatientDTO getPatientById(Integer id) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient not found with id: " + id));
        return convertToDTO(patient);
    }

    //Création d'un patient
    public PatientDTO createPatient(PatientDTO dto) {
        Patient patient = new Patient();
        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setBirthDate(dto.getBirthDate());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        patient.setPhone(dto.getPhone());

        Patient savedPatient = patientRepository.save(patient);
        return convertToDTO(savedPatient);
    }

    //Modification de données patient
    public PatientDTO updatePatient(Integer id, PatientDTO dto) {
        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Patient not found with id: " + id));

        patient.setFirstName(dto.getFirstName());
        patient.setLastName(dto.getLastName());
        patient.setBirthDate(dto.getBirthDate());
        patient.setGender(dto.getGender());
        patient.setAddress(dto.getAddress());
        patient.setPhone(dto.getPhone());

        Patient updatedPatient = patientRepository.save(patient);
        return convertToDTO(updatedPatient);
    }

    //Suppression d'un patient
    public void deletePatient(Integer id) {
        if (!patientRepository.existsById(id)) {
            throw new NotFoundException("Patient not found with id: " + id);
        }

        patientRepository.deleteById(id);
    }

    //Recherche de patient par nom voir prénom si renseigné
    public List<PatientDTO> searchPatients(String lastName, String firstName) {
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("lastName is required");
        }

        List<Patient> patients;

        if (firstName != null && !firstName.isBlank()) {
            patients = patientRepository.findByLastNameAndFirstName(lastName, firstName);
        } else {
            patients = patientRepository.findByLastName(lastName);
        }

        return patients.stream()
                .map(this::convertToDTO)
                .toList();
    }

    // Convertit l’entité Patient en DTO
    private PatientDTO convertToDTO(Patient patient) {

        PatientDTO dto = new PatientDTO(
                patient.getId(),
                patient.getFirstName(),
                patient.getLastName(),
                patient.getBirthDate(),
                patient.getGender(),
                patient.getAddress(),
                patient.getPhone()
        );

        dto.setVersion(patient.getVersion());

        return dto;
    }
}