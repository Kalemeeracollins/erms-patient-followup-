package com.example.backend.followup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {

    // ==========================================
    // 1. DATA RETRIEVAL METHODS (Returns Lists)
    // ==========================================

    List<FollowUp> findByPatient_Id(Long patientId);

    List<FollowUp> findByVisit_Id(Long visitId);

    List<FollowUp> findByStatus(FollowUpStatus status);

    List<FollowUp> findByFollowUpDate(LocalDate followUpDate);

    List<FollowUp> findByFollowUpDateAndStatus(
            LocalDate followUpDate,
            FollowUpStatus status
    );

    List<FollowUp> findByFollowUpDateAfter(LocalDate followUpDate);

    List<FollowUp> findByFollowUpDateBeforeAndStatus(
            LocalDate date,
            FollowUpStatus status
    );


    // ==========================================
    // 2. METRIC / COUNTING METHODS (Returns Long)
    // ==========================================

    long countByStatus(FollowUpStatus status);

    long countByFollowUpDate(LocalDate followUpDate);

    long countByFollowUpDateAndStatus(
            LocalDate followUpDate,
            FollowUpStatus status
    );

    long countByFollowUpDateAfter(LocalDate followUpDate);

    long countByFollowUpDateBeforeAndStatus(
            LocalDate date,
            FollowUpStatus status
    );
}