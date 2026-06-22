package com.example.backend.visit;

import com.example.backend.patient.Patient;
import com.example.backend.patient.PatientRepository;
// Make sure these imports match your actual follow-up model/repository package locations
import com.example.backend.followup.FollowUp;
import com.example.backend.followup.FollowUpRepository;
import com.example.backend.followup.FollowUpStatus; 

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;
    private final FollowUpRepository followUpRepository; // Added dependency

    @Transactional // Ensures database atomicity for both saves
    public VisitResponse createVisit(VisitRequest request) {

        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Visit visit = Visit.builder()
                .visitNumber(generateVisitNumber())
                .patient(patient)
                .visitDate(request.getVisitDate())
                .reasonForVisit(request.getReasonForVisit())
                .diagnosis(request.getDiagnosis())
                .treatment(request.getTreatment())
                .notes(request.getNotes())
                .followUpRequired(request.getFollowUpRequired())
                .nextReviewDate(request.getNextReviewDate())
                .build();

        // Save the primary visit record first
        Visit savedVisit = visitRepository.save(visit);

        // Handle conditional follow-up creation logic
        if (Boolean.TRUE.equals(savedVisit.getFollowUpRequired())) {
            FollowUp followUp = FollowUp.builder()
                    .patient(savedVisit.getPatient())
                    .visit(savedVisit)
                    .followUpDate(savedVisit.getNextReviewDate())
                    .reason(savedVisit.getDiagnosis())
                    .status(FollowUpStatus.PENDING)
                    .notificationSent(false)
                    .build();

            followUpRepository.save(followUp);
        }

        return toResponse(savedVisit);
    }

    public List<VisitResponse> getAllVisits() {
        return visitRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public VisitResponse getVisitById(Long id) {
        return visitRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new RuntimeException("Visit not found"));
    }

    public List<VisitResponse> getVisitsByPatient(Long patientId) {
        return visitRepository.findByPatient_Id(patientId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public VisitResponse updateVisit(Long id, VisitRequest request) {
        Visit visit = visitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Visit not found"));

        if (request.getVisitDate() != null) {
            visit.setVisitDate(request.getVisitDate());
        }
        if (request.getReasonForVisit() != null) {
            visit.setReasonForVisit(request.getReasonForVisit());
        }
        if (request.getDiagnosis() != null) {
            visit.setDiagnosis(request.getDiagnosis());
        }
        if (request.getTreatment() != null) {
            visit.setTreatment(request.getTreatment());
        }
        if (request.getNotes() != null) {
            visit.setNotes(request.getNotes());
        }
        if (request.getFollowUpRequired() != null) {
            visit.setFollowUpRequired(request.getFollowUpRequired());
        }
        if (request.getNextReviewDate() != null) {
            visit.setNextReviewDate(request.getNextReviewDate());
        }

        Visit saved = visitRepository.save(visit);
        syncFollowUpForVisit(saved);
        return toResponse(saved);
    }

    private void syncFollowUpForVisit(Visit visit) {
        List<FollowUp> existing = followUpRepository.findByVisit_Id(visit.getId());
        Optional<FollowUp> activeFollowUp = existing.stream()
                .filter(f -> f.getStatus() == FollowUpStatus.PENDING
                        || f.getStatus() == FollowUpStatus.RESCHEDULED)
                .findFirst();

        if (Boolean.TRUE.equals(visit.getFollowUpRequired()) && visit.getNextReviewDate() != null) {
            if (activeFollowUp.isPresent()) {
                FollowUp followUp = activeFollowUp.get();
                followUp.setFollowUpDate(visit.getNextReviewDate());
                followUp.setReason(visit.getDiagnosis());
                followUpRepository.save(followUp);
            } else {
                FollowUp followUp = FollowUp.builder()
                        .patient(visit.getPatient())
                        .visit(visit)
                        .followUpDate(visit.getNextReviewDate())
                        .reason(visit.getDiagnosis())
                        .status(FollowUpStatus.PENDING)
                        .notificationSent(false)
                        .build();
                followUpRepository.save(followUp);
            }
        } else if (activeFollowUp.isPresent()) {
            FollowUp followUp = activeFollowUp.get();
            followUp.setStatus(FollowUpStatus.CANCELLED);
            followUpRepository.save(followUp);
        }
    }

    private String generateVisitNumber() {
        long count = visitRepository.count() + 1;

        return "VIS-" +
                Year.now().getValue() +
                "-" +
                String.format("%05d", count);
    }

    private VisitResponse toResponse(Visit visit) {
        return VisitResponse.builder()
                .id(visit.getId())
                .visitNumber(visit.getVisitNumber())
                .patientId(visit.getPatient().getId())
                .patientName(
                        visit.getPatient().getFirstName() +
                                " " +
                                visit.getPatient().getLastName()
                )
                .visitDate(visit.getVisitDate())
                .diagnosis(visit.getDiagnosis())
                .followUpRequired(visit.getFollowUpRequired())
                .nextReviewDate(visit.getNextReviewDate())
                .reasonForVisit(visit.getReasonForVisit())
                .treatment(visit.getTreatment())
                .notes(visit.getNotes())
                .build();
    }
}