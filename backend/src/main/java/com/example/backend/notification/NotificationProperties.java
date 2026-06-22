package com.example.backend.notification;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "app.notifications")
public class NotificationProperties {

    /** When true and providers are not configured, log messages and treat as delivered. */
    private boolean demoMode = true;

    private String fromEmail = "kalemeeracollins@outlook.com";
    private String fromName = "Hospital Follow-Up";

    private String twilioAccountSid = "";
    private String twilioAuthToken = "";
    private String twilioPhoneNumber = "";
    private String twilioWhatsappNumber = "";
}
