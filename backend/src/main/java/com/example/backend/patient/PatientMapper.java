package com.example.backend.patient;

public class PatientMapper {

    public static PatientResponse toResponse(Patient patient) {

        return PatientResponse.builder()
                .id(patient.getId())
                .patientNumber(patient.getPatientNumber())
                .fullName(
                        patient.getFirstName() + " " +
                        patient.getLastName()
                )
                .gender(patient.getGender() != null ? patient.getGender().name() : null)
                // .age(patient.getAge())
                .phoneNumber(patient.getPhoneNumber())
                .email(patient.getEmail())
                .active(patient.getActive())
                .build();
    }
}