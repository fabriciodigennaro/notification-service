package com.parkingapp.notificationservice.application.sendemailnotification;

import com.parkingapp.notificationservice.domain.email.EmailNotification;
import com.parkingapp.notificationservice.domain.email.EmailService;
import com.parkingapp.notificationservice.domain.email.EmailTemplate;
import com.parkingapp.notificationservice.domain.email.EmailTemplateRepository;
import com.parkingapp.notificationservice.domain.user.UserRepository;

import java.util.Optional;
import java.util.UUID;

import static com.parkingapp.notificationservice.application.sendemailnotification.SendEmailNotificationResponse.*;

public class SendEmailNotificationUseCase {
    private final EmailTemplateRepository emailTemplateRepository;
    private final UserRepository userRepository;
    private final EmailService emailService;

    public SendEmailNotificationUseCase(
            EmailTemplateRepository emailTemplateRepository,
            UserRepository userRepository,
            EmailService emailService
    ) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    public SendEmailNotificationResponse execute(UUID userId, UUID templateId) {
        Optional<EmailTemplate> emailTemplate = emailTemplateRepository.getEmailTemplateById(templateId);
        Optional<String> userEmailAddress = userRepository.getUserEmailAddressByUserId(userId);

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