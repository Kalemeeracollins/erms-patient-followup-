package com.example.backend.visit;

import com.example.backend.patient.Patient;
import com.example.backend.patient.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VisitService {

    private final VisitRepository visitRepository;
    private final PatientRepository patientRepository;

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

        return toResponse(visitRepository.save(visit));
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
                .build();
    }
}