package com.example.backend.followup;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class FollowUpResponse {

    private Long id;

    private Long patientId;

    private String patientName;

    private Long visitId;

    private LocalDate followUpDate;

    private String reason;

    private String status;

    private Boolean notificationSent;
}