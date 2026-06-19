package com.example.backend.patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    Optional<Patient> findByPatientNumber(String patientNumber);

    Optional<Patient> findByNationalId(String nationalId);

    Optional<Patient> findByPhoneNumber(String phoneNumber);

    boolean existsByNationalId(String nationalId);

    @Query("""
        SELECT p FROM Patient p
        WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(p.patientNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
        OR LOWER(p.phoneNumber) LIKE LOWER(CONCAT('%', :keyword, '%'))
    """)
    List<Patient> searchPatients(String keyword);
}