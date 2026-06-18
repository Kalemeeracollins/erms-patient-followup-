package com.example.backend.followup;

import com.example.backend.patient.Patient;
import com.example.backend.patient.PatientRepository;
import com.example.backend.visit.Visit;
import com.example.backend.visit.VisitRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowUpService {

    private final FollowUpRepository followUpRepository;
    private final PatientRepository patientRepository;
    private final VisitRepository visitRepository;

    /*
     * CREATE FOLLOW-UP
     */
    public FollowUpResponse createFollowUp(FollowUpRequest request) {

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() ->
                        new RuntimeException("Patient not found"));

        Visit visit = visitRepository.findById(request.getVisitId())
                .orElseThrow(() ->
                        new RuntimeException("Visit not found"));

        FollowUp followUp = FollowUp.builder()
                .patient(patient)
                .visit(visit)
                .followUpDate(request.getFollowUpDate())
                .reason(request.getReason())
                .notes(request.getNotes())
                .status(FollowUpStatus.PENDING)
                .notificationSent(false)
                .build();

        FollowUp saved = followUpRepository.save(followUp);

        return mapToResponse(saved);
    }

    /*
     * GET ALL FOLLOWUPS
     */
    public List<FollowUpResponse> getAllFollowUps() {

        return followUpRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /*
     * GET FOLLOWUP BY ID
     */
    public FollowUpResponse getFollowUpById(Long id) {

        FollowUp followUp = followUpRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Follow-up not found"));

        return mapToResponse(followUp);
    }

    /*
     * GET PATIENT FOLLOWUPS
     */
    public List<FollowUpResponse> getPatientFollowUps(Long patientId) {

        return followUpRepository.findByPatient_Id(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /*
     * GET PENDING FOLLOWUPS
     */
    public List<FollowUpResponse> getPendingFollowUps() {

        return followUpRepository.findByStatus(
                        FollowUpStatus.PENDING)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /*
     * GET DUE TODAY
     */
    public List<FollowUpResponse> getDueToday() {

        return followUpRepository.findByFollowUpDateAndStatus(
                        LocalDate.now(),
                        FollowUpStatus.PENDING)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /*
     * COMPLETE FOLLOWUP
     */
    public void completeFollowUp(Long id) {

        FollowUp followUp = followUpRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Follow-up not found"));

        followUp.setStatus(FollowUpStatus.COMPLETED);

        followUpRepository.save(followUp);
    }

    /*
     * MARK MISSED
     */
    public void markMissed(Long id) {

        FollowUp followUp = followUpRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Follow-up not found"));

        followUp.setStatus(FollowUpStatus.MISSED);

        followUpRepository.save(followUp);
    }

     
    /*
     * RESPONSE MAPPER
     */
    private FollowUpResponse mapToResponse(FollowUp followUp) {

        return FollowUpResponse.builder()
                .id(followUp.getId())
                .patientId(followUp.getPatient().getId())
                .patientName(
                        followUp.getPatient().getFirstName()
                                + " "
                                + followUp.getPatient().getLastName()
                )
                .visitId(followUp.getVisit().getId())
                .followUpDate(followUp.getFollowUpDate())
                .reason(followUp.getReason())
                .status(followUp.getStatus().name())
                .notificationSent(
                        followUp.getNotificationSent()
                )
                .build();
    }

    /*
     * RESCHEDULE FOLLOWUP
     */
    public FollowUpResponse rescheduleFollowUp(Long id, LocalDate newDate) {

    FollowUp followUp = followUpRepository.findById(id)
            .orElseThrow(() ->
                    new RuntimeException("Follow-up not found"));

    followUp.setFollowUpDate(newDate);
    followUp.setStatus(FollowUpStatus.RESCHEDULED);

    FollowUp updated = followUpRepository.save(followUp);

    return mapToResponse(updated);
}
}