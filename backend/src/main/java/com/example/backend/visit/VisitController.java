package com.example.backend.visit;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/visits")
@RequiredArgsConstructor
@CrossOrigin(origins = {
        "http://localhost:3000",
        "http://localhost:5173"
})
public class VisitController {

    private final VisitService visitService;

    @PostMapping
    public ResponseEntity<VisitResponse> createVisit(
            @RequestBody VisitRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(visitService.createVisit(request));
    }

    @GetMapping
    public ResponseEntity<List<VisitResponse>> getAllVisits() {

        return ResponseEntity.ok(
                visitService.getAllVisits()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<VisitResponse> getVisitById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                visitService.getVisitById(id)
        );
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<VisitResponse>> getPatientVisits(
            @PathVariable Long patientId) {

        return ResponseEntity.ok(
                visitService.getVisitsByPatient(patientId)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<VisitResponse> updateVisit(
            @PathVariable Long id,
            @RequestBody VisitRequest request) {

        return ResponseEntity.ok(visitService.updateVisit(id, request));
    }
}