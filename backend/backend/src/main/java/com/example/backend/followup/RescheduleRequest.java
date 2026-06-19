package com.example.backend.followup;

import lombok.Data;

import java.time.LocalDate;

@Data
public class RescheduleRequest {

    private LocalDate newDate;

    private String reason;
}