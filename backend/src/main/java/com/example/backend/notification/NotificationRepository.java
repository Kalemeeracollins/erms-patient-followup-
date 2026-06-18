package com.example.backend.notification;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByPatient_Id(Long patientId);

    List<Notification> findBySent(Boolean sent);

}