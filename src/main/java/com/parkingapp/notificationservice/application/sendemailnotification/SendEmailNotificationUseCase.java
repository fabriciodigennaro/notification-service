package com.parkingapp.notificationservice.application.sendemailnotification;

import com.parkingapp.notificationservice.domain.email.EmailNotification;
import com.parkingapp.notificationservice.domain.email.EmailService;
import com.parkingapp.notificationservice.domain.email.EmailTemplate;
import com.parkingapp.notificationservice.domain.email.EmailTemplateRepository;

import java.util.UUID;

public class SendEmailNotificationUseCase {
    private final EmailTemplateRepository emailTemplateRepository;
    private final EmailService emailService;

    public SendEmailNotificationUseCase(
            EmailTemplateRepository emailTemplateRepository,
            EmailService emailService
    ) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.emailService = emailService;
    }

    public void execute(UUID userId, UUID templateId) {
        EmailTemplate emailTemplate = emailTemplateRepository.getEmailTemplateById(templateId);
        String userEmailAddress = "test@email.com";
        EmailNotification emailNotification = new EmailNotification(
                userEmailAddress,
                emailTemplate.getSubject(),
                emailTemplate.getBody()
        );
        this.emailService.send(emailNotification);
    }

}
