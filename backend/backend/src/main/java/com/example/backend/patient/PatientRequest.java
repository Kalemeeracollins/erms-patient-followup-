package com.example.backend.patient;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequest {

    private String firstName;

    private String lastName;

    private Gender gender;

    private LocalDate dateOfBirth;

    private String phoneNumber;

    private String alternativePhoneNumber;

    private String email;

    private String address;

    private String occupation;

    private String nationalId;

    private String emergencyContactName;

    private String emergencyContactPhone;

    private String allergies;

    private String bloodGroup;
}