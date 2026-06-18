package com.example.backend.visit;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class VisitResponse {

    private Long id;

    private String visitNumber;

    private Long patientId;

    private String patientName;

    private LocalDate visitDate;

    private String diagnosis;

    private Boolean followUpRequired;

    private LocalDate nextReviewDate;
}