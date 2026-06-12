package com.medilabo.patient.repository;

import com.medilabo.patient.model.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Integer> {
    Page<Patient> findByLastName(String lastName, Pageable pageable);
    Page<Patient> findByLastNameAndFirstName(String lastName, String firstName, Pageable pageable);
}