package com.medilabo.patient.repository;

import com.medilabo.patient.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    List<Patient> findByLastName(String lastName);
    List<Patient> findByLastNameAndFirstName(String lastName, String firstName);
}