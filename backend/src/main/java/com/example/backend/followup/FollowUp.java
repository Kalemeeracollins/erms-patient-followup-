package com.example.backend.followup;

import com.example.backend.patient.Patient;
import com.example.backend.visit.Visit;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "follow_ups")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FollowUp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "patient_id")
private Patient patient;

@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "visit_id")
private Visit visit;

    private LocalDate followUpDate;

    private String reason;

    @Column(length = 2000)
    private String notes;

    @Enumerated(EnumType.STRING)
    private FollowUpStatus status;

    private Boolean notificationSent;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        notificationSent = false;
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}