package com.example.backend.notification;

import com.example.backend.followup.FollowUp;
import com.example.backend.patient.Patient;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "followup_id")
    private FollowUp followUp;

    @Column(length = 2000)
    private String message;

    @Enumerated(EnumType.STRING)
    private NotificationChannel channel;

    private Boolean sent;

    private LocalDateTime sentAt;

    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {

        sent = false;
        createdAt = LocalDateTime.now();
    }
}