package com.example.backend.followup;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/followups")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:5173"
})
public class FollowUpController {

    private final FollowUpService followUpService;

    /*
     * CREATE ENDPOINT
     */
    @PostMapping
    public ResponseEntity<FollowUpResponse> createFollowUp(
            @RequestBody FollowUpRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(followUpService.createFollowUp(request));
    }

    /*
     * GET ENDPOINTS (RETRIEVAL & FILTERS)
     */
    @GetMapping
    public ResponseEntity<List<FollowUpResponse>> getAllFollowUps() {

        return ResponseEntity.ok(
                followUpService.getAllFollowUps()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<FollowUpResponse> getFollowUpById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                followUpService.getFollowUpById(id)
        );
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<FollowUpResponse>> getPatientFollowUps(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                followUpService.getPatientFollowUps(patientId)
        );
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FollowUpResponse>> getPendingFollowUps() {

        return ResponseEntity.ok(
                followUpService.getPendingFollowUps()
        );
    }

    @GetMapping("/today")
    public ResponseEntity<List<FollowUpResponse>> getTodayFollowUps() {

        return ResponseEntity.ok(
                followUpService.getTodayFollowUps()
        );
    }

    @GetMapping("/upcoming")
    public ResponseEntity<List<FollowUpResponse>> getUpcomingFollowUps() {

        return ResponseEntity.ok(
                followUpService.getUpcomingFollowUps()
        );
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<FollowUpResponse>> getOverdueFollowUps() {

        return ResponseEntity.ok(
                followUpService.getOverdueFollowUps()
        );
    }

    /*
     * PATCH ENDPOINTS (STATUS & DATE UPDATES)
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<String> completeFollowUp(
            @PathVariable Long id) {

        followUpService.completeFollowUp(id);

        return ResponseEntity.ok(
                "Follow-up marked as completed"
        );
    }

    @PatchMapping("/{id}/missed")
    public ResponseEntity<String> markMissed(
            @PathVariable Long id) {

        followUpService.markMissed(id);

        return ResponseEntity.ok(
                "Follow-up marked as missed"
        );
    }

    @PatchMapping("/{id}/reschedule")
    public ResponseEntity<FollowUpResponse> rescheduleFollowUp(
            @PathVariable Long id,
            @RequestBody RescheduleRequest request) {

        return ResponseEntity.ok(
                followUpService.rescheduleFollowUp(
                        id,
                        request.getNewDate()
                )
        );
    }
}