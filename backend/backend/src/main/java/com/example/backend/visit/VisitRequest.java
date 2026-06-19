package com.example.backend.visit;

import lombok.Data;

import java.time.LocalDate;

@Data
public class VisitRequest {

    private Long patientId;

    private LocalDate visitDate;

    private String reasonForVisit;

    private String diagnosis;

    private String treatment;

    private String notes;

    private Boolean followUpRequired;

    private LocalDate nextReviewDate;
}