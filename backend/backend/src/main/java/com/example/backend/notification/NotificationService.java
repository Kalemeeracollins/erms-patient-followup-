package com.example.backend.notification;

import com.example.backend.followup.FollowUp;
import com.example.backend.followup.FollowUpRepository;
import com.example.backend.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FollowUpRepository followUpRepository;

    public NotificationResponse sendNotification(
            NotificationRequest request) {

        FollowUp followUp = followUpRepository.findById(
                        request.getFollowUpId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Follow-up not found"));

        Notification notification =
                Notification.builder()
                        .patient(followUp.getPatient())
                        .followUp(followUp)
                        .message(request.getMessage())
                        .channel(request.getChannel())
                        .sent(true)
                        .sentAt(LocalDateTime.now())
                        .build();

        Notification saved =
                notificationRepository.save(notification);

        return mapToResponse(saved);
    }

    public List<NotificationResponse> getAllNotifications() {

        return notificationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private NotificationResponse mapToResponse(
            Notification notification) {

        return NotificationResponse.builder()
                .id(notification.getId())
                .patientId(
                        notification.getPatient().getId())
                .patientName(
                        notification.getPatient()
                                .getFirstName()
                                + " "
                                + notification.getPatient()
                                .getLastName())
                .followUpId(
                        notification.getFollowUp().getId())
                .message(notification.getMessage())
                .channel(
                        notification.getChannel().name())
                .sent(notification.getSent())
                .sentAt(notification.getSentAt())
                .build();
    }
}