package com.example.backend.patient;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientResponse {

    private Long id;

    private String patientNumber;

    private String fullName;

    private String gender;

    private Integer age;

    private String phoneNumber;

    private String email;

    private Boolean active;

    private Boolean deceased;

    private String dateOfBirth;
}