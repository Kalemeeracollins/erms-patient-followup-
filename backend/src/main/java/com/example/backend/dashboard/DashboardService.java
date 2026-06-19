package com.example.backend.dashboard;

import com.example.backend.followup.FollowUpRepository;
import com.example.backend.followup.FollowUpStatus;
import com.example.backend.patient.PatientRepository;
import com.example.backend.visit.VisitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;
    private final FollowUpRepository followUpRepository;

    public DashboardResponse getStats() {

        return DashboardResponse.builder()
                .totalPatients(patientRepository.count())
                .totalVisits(visitRepository.count())
                .totalFollowUps(followUpRepository.count())

                .pendingFollowUps(
                        followUpRepository.countByStatus(
                                FollowUpStatus.PENDING))

                .completedFollowUps(
                        followUpRepository.countByStatus(
                                FollowUpStatus.COMPLETED))

                .missedFollowUps(
                        followUpRepository.countByStatus(
                                FollowUpStatus.MISSED))

                .todayFollowUps(
                        followUpRepository.countByFollowUpDate(
                                LocalDate.now()))

                .overdueFollowUps(
                        followUpRepository
                                .countByFollowUpDateBeforeAndStatus(
                                        LocalDate.now(),
                                        FollowUpStatus.PENDING))

                .build();
    }
}