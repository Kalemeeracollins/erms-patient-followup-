package com.example.backend.report;

import com.example.backend.followup.FollowUpRepository;
import com.example.backend.followup.FollowUpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final FollowUpRepository followUpRepository;

    public MonthlyReportResponse monthlyReport() {

        long total = followUpRepository.count();

        long completed =
                followUpRepository.countByStatus(
                        FollowUpStatus.COMPLETED);

        long missed =
                followUpRepository.countByStatus(
                        FollowUpStatus.MISSED);

        double returnRate =
                total == 0
                        ? 0
                        : ((double) completed / total) * 100;

        return MonthlyReportResponse.builder()
                .totalFollowUps(total)
                .completedFollowUps(completed)
                .missedFollowUps(missed)
                .returnRate(returnRate)
                .build();
    }
}