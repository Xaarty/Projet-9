package com.medilabo.patient.service;

import com.medilabo.patient.dto.PatientDTO;
import com.medilabo.patient.exception.NotFoundException;
import com.medilabo.patient.model.Patient;
import com.medilabo.patient.repository.PatientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.persistence.Version;

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient patient;
    private PatientDTO patientDTO;

    @BeforeEach
    void setUp() {
        patient = new Patient(
                1,
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "M",
                "1 Main Street",
                "0123456789"
        );

        patientDTO = new PatientDTO(
                1,
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "M",
                "1 Main Street",
                "0123456789"
        );
    }

    @Test
    void shouldReturnAllPatients() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Patient> patientPage = new PageImpl<>(List.of(patient));

        when(patientRepository.findAll(pageable)).thenReturn(patientPage);

        Page<PatientDTO> result = patientService.getAllPatients(0, 20);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("John", result.getContent().get(0).getFirstName());
        assertEquals("Doe", result.getContent().get(0).getLastName());

        verify(patientRepository).findAll(pageable);
    }

    @Test
    void shouldReturnPatientByIdWhenPatientExists() {
        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));

        PatientDTO result = patientService.getPatientById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(patientRepository).findById(1);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGettingPatientByIdAndPatientDoesNotExist() {
        when(patientRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> patientService.getPatientById(1)
        );

        assertEquals("Patient not found with id: 1", exception.getMessage());
        verify(patientRepository).findById(1);
    }

    @Test
    void shouldCreatePatient() {
        PatientDTO inputDto = new PatientDTO(
                null,
                "John",
                "Doe",
                LocalDate.of(1990, 1, 1),
                "M",
                "1 Main Street",
                "0123456789"
        );

        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        PatientDTO result = patientService.createPatient(inputDto);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());

        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void shouldUpdatePatientWhenPatientExists() {
        PatientDTO updateDto = new PatientDTO(
                null,
                "Jane",
                "Doe",
                LocalDate.of(1992, 2, 2),
                "F",
                "2 New Street",
                "0987654321"
        );

        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PatientDTO result = patientService.updatePatient(1, updateDto);

        assertNotNull(result);
        assertEquals("Jane", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals(LocalDate.of(1992, 2, 2), result.getBirthDate());
        assertEquals("F", result.getGender());
        assertEquals("2 New Street", result.getAddress());
        assertEquals("0987654321", result.getPhone());

        verify(patientRepository).findById(1);
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingPatientAndPatientDoesNotExist() {
        when(patientRepository.findById(1)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> patientService.updatePatient(1, patientDTO)
        );

        assertEquals("Patient not found with id: 1", exception.getMessage());
        verify(patientRepository).findById(1);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void shouldDeletePatientWhenPatientExists() {
        when(patientRepository.existsById(1)).thenReturn(true);

        patientService.deletePatient(1);

        verify(patientRepository).existsById(1);
        verify(patientRepository).deleteById(1);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingPatientAndPatientDoesNotExist() {
        when(patientRepository.existsById(1)).thenReturn(false);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> patientService.deletePatient(1)
        );

        assertEquals("Patient not found with id: 1", exception.getMessage());
        verify(patientRepository).existsById(1);
        verify(patientRepository, never()).deleteById(anyInt());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenSearchingPatientsWithoutLastName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> patientService.searchPatients(null, "John")
        );

        assertEquals("lastName is required", exception.getMessage());
        verify(patientRepository, never()).findByLastName(anyString());
        verify(patientRepository, never()).findByLastNameAndFirstName(anyString(), anyString());
    }

    @Test
    void shouldSearchPatientsByLastNameOnly() {
        when(patientRepository.findByLastName("Doe")).thenReturn(List.of(patient));

        List<PatientDTO> result = patientService.searchPatients("Doe", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Doe", result.get(0).getLastName());

        verify(patientRepository).findByLastName("Doe");
        verify(patientRepository, never()).findByLastNameAndFirstName(anyString(), anyString());
    }

    @Test
    void shouldSearchPatientsByLastNameAndFirstName() {
        when(patientRepository.findByLastNameAndFirstName("Doe", "John")).thenReturn(List.of(patient));

        List<PatientDTO> result = patientService.searchPatients("Doe", "John");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals("John", result.get(0).getFirstName());

        verify(patientRepository).findByLastNameAndFirstName("Doe", "John");
    }

    @Test
    void shouldThrowConflictWhenPatientIsUpdatedConcurrently() {
        Patient patient = new Patient();
        patient.setId(1);
        patient.setFirstName("John");
        patient.setLastName("Doe");
        patient.setVersion(1L);

        PatientDTO dto = new PatientDTO();
        dto.setId(1);
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setVersion(1L);

        when(patientRepository.findById(1)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class)))
                .thenThrow(new ObjectOptimisticLockingFailureException(Patient.class, 1));

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            patientService.updatePatient(1, dto);
        });
    }
}
