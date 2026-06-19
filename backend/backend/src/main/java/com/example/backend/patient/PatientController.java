package com.example.backend.patient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Year;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/patients")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"}, allowCredentials = "true", maxAge = 3600)
public class PatientController {

    private final PatientRepository patientRepository;

    @GetMapping
    public ResponseEntity<List<PatientResponse>> getAllPatients() {
        log.info("Fetching all patients");
        List<PatientResponse> responses = patientRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PatientResponse> getPatientById(@PathVariable Long id) {
        log.info("Fetching patient with id: {}", id);
        return patientRepository.findById(id)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/number/{patientNumber}")
    public ResponseEntity<PatientResponse> getPatientByNumber(@PathVariable String patientNumber) {
        log.info("Fetching patient with patientNumber: {}", patientNumber);
        return patientRepository.findByPatientNumber(patientNumber)
                .map(this::toResponse)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<PatientResponse> createPatient(@RequestBody PatientRequest request) {
        log.info("Creating patient: {} {}", request.getFirstName(), request.getLastName());

        if (request.getNationalId() != null && patientRepository.existsByNationalId(request.getNationalId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Patient patient = Patient.builder()
                .patientNumber(generatePatientNumber())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .gender(request.getGender())
                .dateOfBirth(request.getDateOfBirth())
                .phoneNumber(request.getPhoneNumber())
                .alternativePhoneNumber(request.getAlternativePhoneNumber())
                .email(request.getEmail())
                .address(request.getAddress())
                .occupation(request.getOccupation())
                .nationalId(request.getNationalId())
                .emergencyContactName(request.getEmergencyContactName())
                .emergencyContactPhone(request.getEmergencyContactPhone())
                .allergies(request.getAllergies())
                .bloodGroup(request.getBloodGroup())
                .active(true)
                .deceased(false)
                .build();

        Patient saved = patientRepository.save(patient);
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PatientResponse> updatePatient(@PathVariable Long id, @RequestBody PatientRequest request) {
        log.info("Updating patient with id: {}", id);

        return patientRepository.findById(id)
                .map(existing -> {
                    existing.setFirstName(request.getFirstName());
                    existing.setLastName(request.getLastName());
                    existing.setGender(request.getGender());
                    existing.setDateOfBirth(request.getDateOfBirth());
                    existing.setPhoneNumber(request.getPhoneNumber());
                    existing.setAlternativePhoneNumber(request.getAlternativePhoneNumber());
                    existing.setEmail(request.getEmail());
                    existing.setAddress(request.getAddress());
                    existing.setOccupation(request.getOccupation());
                    existing.setNationalId(request.getNationalId());
                    existing.setEmergencyContactName(request.getEmergencyContactName());
                    existing.setEmergencyContactPhone(request.getEmergencyContactPhone());
                    existing.setAllergies(request.getAllergies());
                    existing.setBloodGroup(request.getBloodGroup());
                    Patient updated = patientRepository.save(existing);
                    return ResponseEntity.ok(toResponse(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    private String generatePatientNumber() {
        long count = patientRepository.count() + 1;
        return "PAT-" + Year.now().getValue() + "-" + String.format("%05d", count);
    }

    private PatientResponse toResponse(Patient patient) {

    Integer age = null;

    if (patient.getDateOfBirth() != null) {
        age = java.time.Period.between(
                patient.getDateOfBirth(),
                java.time.LocalDate.now()
        ).getYears();
    }

    return PatientResponse.builder()
            .id(patient.getId())
            .patientNumber(patient.getPatientNumber())
            .fullName(
                    patient.getFirstName() + " " +
                    patient.getLastName()
            )
            .gender(
                    patient.getGender() != null
                            ? patient.getGender().name()
                            : null
            )
            .age(age)
            .phoneNumber(patient.getPhoneNumber())
            .email(patient.getEmail())
            .active(patient.getActive())
            .build();
        }
}