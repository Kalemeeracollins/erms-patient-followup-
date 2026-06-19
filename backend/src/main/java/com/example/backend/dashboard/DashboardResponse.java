package com.example.backend.dashboard;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardResponse {

    private Long totalPatients;
    private Long totalVisits;
    private Long totalFollowUps;

    private Long pendingFollowUps;
    private Long completedFollowUps;
    private Long missedFollowUps;

    private Long todayFollowUps;
    private Long overdueFollowUps;
}