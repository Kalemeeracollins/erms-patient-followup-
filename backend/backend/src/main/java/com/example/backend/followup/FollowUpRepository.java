package com.example.backend.followup;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface FollowUpRepository extends JpaRepository<FollowUp, Long> {

    List<FollowUp> findByPatient_Id(Long patientId);

    List<FollowUp> findByVisit_Id(Long visitId);

    List<FollowUp> findByStatus(FollowUpStatus status);

    List<FollowUp> findByFollowUpDate(LocalDate followUpDate);

    List<FollowUp> findByFollowUpDateAndStatus(
            LocalDate followUpDate,
            FollowUpStatus status
    );
}