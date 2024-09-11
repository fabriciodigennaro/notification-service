package com.parkingapp.notificationservice.application.sendemailnotification;

import com.parkingapp.notificationservice.domain.email.EmailService;
import com.parkingapp.notificationservice.domain.email.EmailTemplate;
import com.parkingapp.notificationservice.domain.email.EmailTemplateRepository;
import com.parkingapp.notificationservice.domain.exceptions.EmailTemplateNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SendEmailNotificationUseCaseTest {
    private final EmailTemplateRepository emailTemplateRepository = mock(EmailTemplateRepository.class);
    private final EmailService emailService = mock(EmailService.class);
    private final SendEmailNotificationUseCase useCase = new SendEmailNotificationUseCase(emailTemplateRepository, emailService);

    UUID userId = UUID.randomUUID();
    UUID templateId = UUID.randomUUID();
    String subject = "test subject";
    String body = "test body";
    private final EmailTemplate emailTemplate = new EmailTemplate(
            templateId,
            subject,
            body
    );

    @Test
    void shouldSendAnEmailNotification() {
        // GIVEN
        when(emailTemplateRepository.getEmailTemplateById(templateId)).thenReturn(Optional.of(emailTemplate));

        // WHEN
        useCase.execute(userId, templateId);

        // THEN
        verify(emailTemplateRepository).getEmailTemplateById(templateId);
    }

    @Test
    void shouldThrowEmailTemplateNotFoundExceptionWhenTemplateNotExists() {
        // GIVEN
        when(emailTemplateRepository.getEmailTemplateById(templateId)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(EmailTemplateNotFoundException.class, () -> {
            useCase.execute(userId, templateId);
        });
        verify(emailTemplateRepository).getEmailTemplateById(templateId);
    }
}