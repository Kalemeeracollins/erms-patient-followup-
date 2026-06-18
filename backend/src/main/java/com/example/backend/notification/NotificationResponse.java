package com.example.backend.notification;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class NotificationResponse {

    private Long id;

    private Long patientId;

    private String patientName;

    private Long followUpId;

    private String message;

    private String channel;

    private Boolean sent;

    private LocalDateTime sentAt;
}