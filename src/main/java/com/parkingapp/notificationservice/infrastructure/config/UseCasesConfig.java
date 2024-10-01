package com.parkingapp.notificationservice.infrastructure.config;

import com.parkingapp.notificationservice.application.sendemailnotification.SendEmailNotificationUseCase;
import com.parkingapp.notificationservice.domain.email.EmailService;
import com.parkingapp.notificationservice.domain.email.EmailTemplateRepository;
import com.parkingapp.notificationservice.domain.user.UserFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

    @Bean
    public SendEmailNotificationUseCase sendEmailNotificationUseCase(
            EmailTemplateRepository emailTemplateRepository,
            UserFetcher userFetcher,
            EmailService emailService
    ) {
        return new SendEmailNotificationUseCase(emailTemplateRepository, userFetcher, emailService);
    }
}
