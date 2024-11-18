package com.parkingapp.notificationservice.domain.email;

public interface EmailService {
    NotificationResult send(EmailNotification emailNotification);
}
