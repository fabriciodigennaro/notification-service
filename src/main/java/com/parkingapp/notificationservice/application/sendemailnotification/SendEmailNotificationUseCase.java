package com.parkingapp.notificationservice.application.sendemailnotification;

import com.parkingapp.notificationservice.domain.email.EmailNotification;
import com.parkingapp.notificationservice.domain.email.EmailService;
import com.parkingapp.notificationservice.domain.email.EmailTemplate;
import com.parkingapp.notificationservice.domain.email.EmailTemplateRepository;

import java.util.Optional;
import java.util.UUID;

import static com.parkingapp.notificationservice.application.sendemailnotification.SendEmailNotificationResponse.*;

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

    public SendEmailNotificationResponse execute(UUID userId, UUID templateId) {
        Optional<EmailTemplate> emailTemplate = emailTemplateRepository.getEmailTemplateById(templateId);
        Optional<String> userEmailAddress = Optional.of("test@email.com");

        if (emailTemplate.isEmpty()) {
            return new EmailTemplateFoundFailure();
        }

        if (userEmailAddress.isEmpty()) {
            return new UserEmailAddressFailure();
        }

        EmailNotification emailNotification = new EmailNotification(
                userEmailAddress.get(),
                emailTemplate.get().getSubject(),
                emailTemplate.get().getBody()
        );
        this.emailService.send(emailNotification);
        return new Successful();
    }
}