package com.example.backend.notification;

import lombok.Data;

@Data
public class NotificationRequest {

    private Long followUpId;

    private String message;

    private NotificationChannel channel;
}