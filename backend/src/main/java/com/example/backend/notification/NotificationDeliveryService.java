package com.example.backend.notification;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationDeliveryService {

    private final NotificationProperties properties;
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String mailUsername;

    public boolean deliver(NotificationChannel channel, String recipient, String message) {
        if (!StringUtils.hasText(recipient)) {
            log.warn("[{}] No recipient provided", channel);
            return false;
        }

        try {
            return switch (channel) {
                case EMAIL -> sendEmail(recipient, message);
                case SMS -> sendSms(recipient, message);
                case WHATSAPP -> sendWhatsApp(recipient, message);
            };
        } catch (Exception ex) {
            log.error("[{}] Delivery failed to {}: {}", channel, recipient, ex.getMessage());
            return false;
        }
    }

    public String resolveRecipient(NotificationChannel channel, String phone, String email) {
        return switch (channel) {
            case EMAIL -> StringUtils.hasText(email) ? email : phone;
            default -> StringUtils.hasText(phone) ? phone : email;
        };
    }

    private boolean sendEmail(String to, String message) {
        if (!isMailConfigured()) {
            return demoDeliver("EMAIL", to, message);
        }

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, StandardCharsets.UTF_8.name());
            helper.setFrom(properties.getFromEmail(), properties.getFromName());
            helper.setTo(to);
            helper.setSubject("Follow-Up Reminder");
            helper.setText(message, false);
            mailSender.send(mimeMessage);
            log.info("[EMAIL] Sent to {}", to);
            return true;
        } catch (Exception ex) {
            log.error("[EMAIL] Failed to send to {}: {}", to, ex.getMessage());
            return false;
        }
    }

    private boolean sendSms(String to, String message) {
        if (!isTwilioConfigured()) {
            return demoDeliver("SMS", to, message);
        }

        String normalized = normalizePhone(to);
        String body = "From=" + encode(properties.getTwilioPhoneNumber())
                + "&To=" + encode(normalized)
                + "&Body=" + encode(message);

        return postTwilio("/2010-04-01/Accounts/" + properties.getTwilioAccountSid() + "/Messages.json", body);
    }

    private boolean sendWhatsApp(String to, String message) {
        if (!isTwilioConfigured()) {
            return demoDeliver("WHATSAPP", to, message);
        }

        String normalized = "whatsapp:" + normalizePhone(to).replace("whatsapp:", "");
        String from = properties.getTwilioWhatsappNumber().startsWith("whatsapp:")
                ? properties.getTwilioWhatsappNumber()
                : "whatsapp:" + properties.getTwilioWhatsappNumber();

        String body = "From=" + encode(from)
                + "&To=" + encode(normalized)
                + "&Body=" + encode(message);

        return postTwilio("/2010-04-01/Accounts/" + properties.getTwilioAccountSid() + "/Messages.json", body);
    }

    private boolean postTwilio(String path, String formBody) {
        try {
            String credentials = properties.getTwilioAccountSid() + ":" + properties.getTwilioAuthToken();
            String auth = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.twilio.com" + path))
                    .header("Authorization", "Basic " + auth)
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(formBody))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                log.info("[TWILIO] Message accepted: {}", response.body());
                return true;
            }

            log.error("[TWILIO] Error {}: {}", response.statusCode(), response.body());
            return false;
        } catch (Exception ex) {
            log.error("[TWILIO] Request failed: {}", ex.getMessage());
            return false;
        }
    }

    private boolean demoDeliver(String channel, String recipient, String message) {
        if (properties.isDemoMode()) {
            log.info("[{}][DEMO] To: {} | Message: {}", channel, recipient, message);
            return true;
        }
        log.warn("[{}] Provider not configured. Set mail/Twilio credentials or enable demo mode.", channel);
        return false;
    }

    private boolean isMailConfigured() {
        return StringUtils.hasText(mailUsername)
                && StringUtils.hasText(properties.getFromEmail())
                && !properties.getFromEmail().endsWith("@hospital.local");
    }

    private boolean isTwilioConfigured() {
        return StringUtils.hasText(properties.getTwilioAccountSid())
                && StringUtils.hasText(properties.getTwilioAuthToken())
                && StringUtils.hasText(properties.getTwilioPhoneNumber());
    }

    private String normalizePhone(String phone) {
        String cleaned = phone.trim();
        if (!cleaned.startsWith("+")) {
            cleaned = "+" + cleaned.replaceAll("[^0-9]", "");
        }
        return cleaned;
    }

    private String encode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
