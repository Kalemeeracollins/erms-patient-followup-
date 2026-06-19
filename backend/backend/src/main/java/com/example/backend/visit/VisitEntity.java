package com.example.backend.visit;

import com.example.backend.patient.Patient;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "visits")
public class VisitEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String visitNumber;

    @ManyToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;

    private LocalDate visitDate;

    private String reasonForVisit;

    private String diagnosis;

    private String treatment;

    private String notes;

    private LocalDate nextReviewDate;

    private Boolean followUpRequired;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}