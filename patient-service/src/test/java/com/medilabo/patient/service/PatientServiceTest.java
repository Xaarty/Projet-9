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

@ExtendWith(MockitoExtension.class)
class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    private Patient lucasPatient;
    private PatientDTO lucasDto;

    @BeforeEach
    void setUp() {
        lucasPatient = new Patient(
                101,
                "Lucas",
                "B",
                LocalDate.of(1988, 5, 12),
                "M",
                "10 Oak Street",
                "0102030405"
        );
        lucasPatient.setVersion(1L);

        lucasDto = new PatientDTO(
                101,
                "Lucas",
                "B",
                LocalDate.of(1988, 5, 12),
                "M",
                "10 Oak Street",
                "0102030405"
        );
        lucasDto.setVersion(1L);
    }

    @Test
    void shouldReturnAllPatients() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Patient> patientPage = new PageImpl<>(List.of(lucasPatient));

        when(patientRepository.findAll(pageable)).thenReturn(patientPage);

        Page<PatientDTO> result = patientService.getAllPatients(0, 20);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals("Lucas", result.getContent().get(0).getFirstName());
        assertEquals("B", result.getContent().get(0).getLastName());
        assertEquals(1L, result.getContent().get(0).getVersion());

        verify(patientRepository).findAll(pageable);
    }

    @Test
    void shouldReturnPatientByIdWhenPatientExists() {
        when(patientRepository.findById(101)).thenReturn(Optional.of(lucasPatient));

        PatientDTO result = patientService.getPatientById(101);

        assertNotNull(result);
        assertEquals(101, result.getId());
        assertEquals("Lucas", result.getFirstName());
        assertEquals("B", result.getLastName());
        assertEquals(1L, result.getVersion());

        verify(patientRepository).findById(101);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenGettingPatientByIdAndPatientDoesNotExist() {
        when(patientRepository.findById(205)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> patientService.getPatientById(205)
        );

        assertEquals("Patient not found with id: 205", exception.getMessage());
        verify(patientRepository).findById(205);
    }

    @Test
    void shouldCreatePatient() {
        PatientDTO inputDto = new PatientDTO(
                null,
                "Emma",
                "K",
                LocalDate.of(1995, 8, 3),
                "F",
                "22 Pine Avenue",
                "0607080910"
        );

        Patient savedPatient = new Patient(
                205,
                "Emma",
                "K",
                LocalDate.of(1995, 8, 3),
                "F",
                "22 Pine Avenue",
                "0607080910"
        );
        savedPatient.setVersion(0L);

        when(patientRepository.save(any(Patient.class))).thenReturn(savedPatient);

        PatientDTO result = patientService.createPatient(inputDto);

        assertNotNull(result);
        assertEquals(205, result.getId());
        assertEquals("Emma", result.getFirstName());
        assertEquals("K", result.getLastName());
        assertEquals(0L, result.getVersion());

        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void shouldUpdatePatientWhenPatientExists() {
        PatientDTO updateDto = new PatientDTO(
                null,
                "Noah",
                "RT",
                LocalDate.of(1979, 11, 20),
                "M",
                "5 River Road",
                "0111222333"
        );

        when(patientRepository.findById(101)).thenReturn(Optional.of(lucasPatient));
        when(patientRepository.save(any(Patient.class))).thenAnswer(invocation -> invocation.getArgument(0));

        PatientDTO result = patientService.updatePatient(101, updateDto);

        assertNotNull(result);
        assertEquals("Noah", result.getFirstName());
        assertEquals("RT", result.getLastName());
        assertEquals(LocalDate.of(1979, 11, 20), result.getBirthDate());
        assertEquals("M", result.getGender());
        assertEquals("5 River Road", result.getAddress());
        assertEquals("0111222333", result.getPhone());

        verify(patientRepository).findById(101);
        verify(patientRepository).save(any(Patient.class));
    }

    @Test
    void shouldThrowNotFoundExceptionWhenUpdatingPatientAndPatientDoesNotExist() {
        PatientDTO updateDto = new PatientDTO(
                null,
                "Lea",
                "V",
                LocalDate.of(2001, 3, 14),
                "F",
                "14 Lake Street",
                "0708091011"
        );

        when(patientRepository.findById(309)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> patientService.updatePatient(309, updateDto)
        );

        assertEquals("Patient not found with id: 309", exception.getMessage());
        verify(patientRepository).findById(309);
        verify(patientRepository, never()).save(any(Patient.class));
    }

    @Test
    void shouldDeletePatientWhenPatientExists() {
        when(patientRepository.existsById(412)).thenReturn(true);

        patientService.deletePatient(412);

        verify(patientRepository).existsById(412);
        verify(patientRepository).deleteById(412);
    }

    @Test
    void shouldThrowNotFoundExceptionWhenDeletingPatientDoesNotExist() {
        when(patientRepository.existsById(518)).thenReturn(false);

        NotFoundException exception = assertThrows(
                NotFoundException.class,
                () -> patientService.deletePatient(518)
        );

        assertEquals("Patient not found with id: 518", exception.getMessage());
        verify(patientRepository).existsById(518);
        verify(patientRepository, never()).deleteById(anyInt());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenSearchingPatientsWithoutLastName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> patientService.searchPatients(null, "Clara")
        );

        assertEquals("lastName is required", exception.getMessage());
        verify(patientRepository, never()).findByLastName(anyString());
        verify(patientRepository, never()).findByLastNameAndFirstName(anyString(), anyString());
    }

    @Test
    void shouldThrowIllegalArgumentExceptionWhenSearchingPatientsWithBlankLastName() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> patientService.searchPatients(" ", "Clara")
        );

        assertEquals("lastName is required", exception.getMessage());
        verify(patientRepository, never()).findByLastName(anyString());
        verify(patientRepository, never()).findByLastNameAndFirstName(anyString(), anyString());
    }

    @Test
    void shouldSearchPatientsByLastNameOnly() {
        Patient hugoPatient = new Patient(
                623,
                "Hugo",
                "M",
                LocalDate.of(1983, 6, 9),
                "M",
                "18 Forest Lane",
                "0203040506"
        );

        when(patientRepository.findByLastName("M")).thenReturn(List.of(hugoPatient));

        List<PatientDTO> result = patientService.searchPatients("M", null);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Hugo", result.get(0).getFirstName());
        assertEquals("M", result.get(0).getLastName());

        verify(patientRepository).findByLastName("M");
        verify(patientRepository, never()).findByLastNameAndFirstName(anyString(), anyString());
    }

    @Test
    void shouldSearchPatientsByLastNameAndFirstName() {
        Patient claraPatient = new Patient(
                734,
                "Clara",
                "Z",
                LocalDate.of(1992, 12, 1),
                "F",
                "7 Hill Road",
                "0304050607"
        );

        when(patientRepository.findByLastNameAndFirstName("Z", "Clara"))
                .thenReturn(List.of(claraPatient));

        List<PatientDTO> result = patientService.searchPatients("Z", "Clara");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Clara", result.get(0).getFirstName());
        assertEquals("Z", result.get(0).getLastName());

        verify(patientRepository).findByLastNameAndFirstName("Z", "Clara");
        verify(patientRepository, never()).findByLastName("Z");
    }

    @Test
    void shouldThrowConflictWhenPatientIsUpdatedConcurrently() {
        PatientDTO updateDto = new PatientDTO();
        updateDto.setId(845);
        updateDto.setFirstName("Alice");
        updateDto.setLastName("D");
        updateDto.setBirthDate(LocalDate.of(1986, 7, 19));
        updateDto.setGender("F");
        updateDto.setAddress("9 Cedar Avenue");
        updateDto.setPhone("0405060708");
        updateDto.setVersion(1L);

        Patient existingPatient = new Patient(
                845,
                "Alice",
                "D",
                LocalDate.of(1986, 7, 19),
                "F",
                "9 Cedar Avenue",
                "0405060708"
        );
        existingPatient.setVersion(1L);

        when(patientRepository.findById(845)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(any(Patient.class)))
                .thenThrow(new ObjectOptimisticLockingFailureException(Patient.class, 845));

        assertThrows(ObjectOptimisticLockingFailureException.class, () -> {
            patientService.updatePatient(845, updateDto);
        });

        verify(patientRepository).findById(845);
        verify(patientRepository).save(any(Patient.class));
    }
}