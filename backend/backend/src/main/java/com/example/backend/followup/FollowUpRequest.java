package com.example.backend.followup;

import lombok.Data;

import java.time.LocalDate;

@Data
public class FollowUpRequest {

    private Long patientId;

    private Long visitId;

    private LocalDate followUpDate;

    private String reason;

    private String notes;
}