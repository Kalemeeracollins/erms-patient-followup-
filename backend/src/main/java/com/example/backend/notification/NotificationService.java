package com.example.backend.notification;

import com.example.backend.followup.FollowUp;
import com.example.backend.followup.FollowUpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final FollowUpRepository followUpRepository;
    private final NotificationDeliveryService deliveryService;

    public NotificationResponse sendNotification(NotificationRequest request) {
        FollowUp followUp = followUpRepository.findById(request.getFollowUpId())
                .orElseThrow(() -> new RuntimeException("Follow-up not found"));

        String recipient = deliveryService.resolveRecipient(
                request.getChannel(),
                followUp.getPatient().getPhoneNumber(),
                followUp.getPatient().getEmail()
        );

        boolean delivered = deliveryService.deliver(request.getChannel(), recipient, request.getMessage());

        Notification notification = Notification.builder()
                .patient(followUp.getPatient())
                .followUp(followUp)
                .message(request.getMessage())
                .channel(request.getChannel())
                .sent(delivered)
                .sentAt(delivered ? LocalDateTime.now() : null)
                .build();

        Notification saved = notificationRepository.save(notification);

        if (delivered) {
            followUp.setNotificationSent(true);
            followUpRepository.save(followUp);
        }

        return mapToResponse(saved);
    }

    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * ADDED: Fetch a single notification by ID and map it to a response DTO
     */
    public NotificationResponse getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification audit log record not found for ID: " + id));
        return mapToResponse(notification);
    }

    public List<NotificationResponse> getPatientNotifications(Long patientId) {
        return notificationRepository.findByPatient_Id(patientId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public long countPendingNotifications() {
        return notificationRepository.countBySent(false);
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return NotificationResponse.builder()
                .id(notification.getId())
                .patientId(notification.getPatient().getId())
                .patientName(
                        notification.getPatient().getFirstName()
                                + " "
                                + notification.getPatient().getLastName())
                .followUpId(notification.getFollowUp().getId())
                .message(notification.getMessage())
                .channel(notification.getChannel().name())
                .sent(notification.getSent())
                .sentAt(notification.getSentAt())
                .build();
    }
}