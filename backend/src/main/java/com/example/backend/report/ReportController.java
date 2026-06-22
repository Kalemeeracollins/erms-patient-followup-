package com.example.backend.report;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/monthly")
    public ResponseEntity<MonthlyReportResponse>
    getMonthlyReport() {

        return ResponseEntity.ok(
                reportService.monthlyReport()
        );
    }
}