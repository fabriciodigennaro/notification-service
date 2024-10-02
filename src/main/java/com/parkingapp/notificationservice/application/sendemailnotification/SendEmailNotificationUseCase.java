package com.parkingapp.notificationservice.application.sendemailnotification;

import com.parkingapp.notificationservice.domain.email.EmailNotification;
import com.parkingapp.notificationservice.domain.email.EmailRequest;
import com.parkingapp.notificationservice.domain.email.EmailService;
import com.parkingapp.notificationservice.domain.email.EmailTemplate;
import com.parkingapp.notificationservice.domain.email.EmailTemplateRepository;
import com.parkingapp.notificationservice.domain.user.User;
import com.parkingapp.notificationservice.domain.user.UserFetcher;

import java.util.Map;
import java.util.Optional;

import static com.parkingapp.notificationservice.application.sendemailnotification.SendEmailNotificationResponse.*;

public class SendEmailNotificationUseCase {
    private final EmailTemplateRepository emailTemplateRepository;
    private final UserFetcher userFetcher;
    private final EmailService emailService;

    public SendEmailNotificationUseCase(
            EmailTemplateRepository emailTemplateRepository,
            UserFetcher userFetcher,
            EmailService emailService
    ) {
        this.emailTemplateRepository = emailTemplateRepository;
        this.userFetcher = userFetcher;
        this.emailService = emailService;
    }

    public SendEmailNotificationResponse execute(EmailRequest request) {
        Optional<EmailTemplate> emailTemplate = emailTemplateRepository.getEmailTemplateById(request.getTemplateId());
        Optional<User> user = userFetcher.fetch(request.getUserId());
        Map<String, Object> params = request.getParams();

        if (emailTemplate.isEmpty()) {
            return new EmailTemplateFoundFailure();
        }

        if (user.isEmpty()) {
            return new UserEmailAddressFailure();
        }
        params.put("name", user.get().getName());

        EmailNotification emailNotification = new EmailNotification(
                user.get().getEmail(),
                emailTemplate.get().getSubject(),
                emailTemplate.get().getBody(),
                params
        );
        try {
            this.emailService.send(emailNotification);
            return new Successful();
        } catch (Exception e) {
            return new Failure();
        }
    }
}