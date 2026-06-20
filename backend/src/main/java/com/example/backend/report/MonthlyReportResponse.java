package com.example.backend.report;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MonthlyReportResponse {

    private Long totalFollowUps;

    private Long completedFollowUps;

    private Long missedFollowUps;

    private Double returnRate;
}