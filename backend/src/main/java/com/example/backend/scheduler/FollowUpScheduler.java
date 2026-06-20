package com.example.backend.scheduler;

import com.example.backend.followup.FollowUp;
import com.example.backend.followup.FollowUpRepository;
import com.example.backend.followup.FollowUpStatus;
import com.example.backend.notification.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FollowUpScheduler {

    private final FollowUpRepository followUpRepository;
    private final NotificationRepository notificationRepository;

    @Scheduled(cron = "0 0 8 * * *")
    public void generateReminders() {

        List<FollowUp> followUps =
                followUpRepository.findByFollowUpDateAndStatus(
                        LocalDate.now().plusDays(1),
                        FollowUpStatus.PENDING
                );

        for (FollowUp followUp : followUps) {

            Notification notification =
                    Notification.builder()
                            .patient(followUp.getPatient())
                            .followUp(followUp)
                            .message(
                                    "Reminder: Follow-up visit scheduled for tomorrow."
                            )
                            .channel(NotificationChannel.SMS)
                            .sent(false)
                            .build();

            notificationRepository.save(notification);

            log.info("Reminder created for patient {}",
                    followUp.getPatient().getPatientNumber());
        }
    }
}