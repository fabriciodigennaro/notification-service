package com.parkingapp.notificationservice.application.sendemailnotification;

import com.parkingapp.notificationservice.domain.email.EmailNotification;
import com.parkingapp.notificationservice.domain.email.EmailService;
import com.parkingapp.notificationservice.domain.email.EmailTemplate;
import com.parkingapp.notificationservice.domain.email.EmailTemplateRepository;
import com.parkingapp.notificationservice.domain.user.User;
import com.parkingapp.notificationservice.domain.user.UserFetcher;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static com.parkingapp.notificationservice.application.sendemailnotification.SendEmailNotificationResponse.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SendEmailNotificationUseCaseTest {
    private final EmailTemplateRepository emailTemplateRepository = mock(EmailTemplateRepository.class);
    private final UserFetcher userFetcher = mock(UserFetcher.class);
    private final EmailService emailService = mock(EmailService.class);
    private final SendEmailNotificationUseCase useCase = new SendEmailNotificationUseCase(emailTemplateRepository, userFetcher, emailService);

    UUID userId = UUID.randomUUID();
    UUID templateId = UUID.randomUUID();
    String userEmailAddress = "test@email.com";
    String subject = "test subject";
    String body = "test body";
    private final EmailTemplate emailTemplate = new EmailTemplate(
            templateId,
            subject,
            body
    );
    private final User user = new User(
            userId,
            userEmailAddress
    );

    @Test
    void shouldSendAnEmailNotification() {
        // GIVEN
        String userEmailAddress = "test@email.com";
        EmailNotification emailNotification = new EmailNotification(
                userEmailAddress,
                emailTemplate.getSubject(),
                emailTemplate.getBody()
        );
        when(emailTemplateRepository.getEmailTemplateById(templateId)).thenReturn(Optional.of(emailTemplate));
        when(userFetcher.fetch(userId)).thenReturn(Optional.of(user));

        // WHEN
        SendEmailNotificationResponse result = useCase.execute(userId, templateId);

        // THEN
        assertThat(result).isInstanceOf(Successful.class);
        verify(emailTemplateRepository).getEmailTemplateById(templateId);
        verify(userFetcher).fetch(userId);
        verify(emailService).send(emailNotification);
    }

    @Test
    void shouldReturnAEmailTemplateFoundFailureIfEmailTemplateNotExists() {
        // GIVEN
        String userEmailAddress = "test@email.com";
        EmailNotification emailNotification = new EmailNotification(
                userEmailAddress,
                emailTemplate.getSubject(),
                emailTemplate.getBody()
        );
        when(emailTemplateRepository.getEmailTemplateById(templateId)).thenReturn(Optional.empty());
        when(userFetcher.fetch(userId)).thenReturn(Optional.of(user));

        // WHEN
        SendEmailNotificationResponse result = useCase.execute(userId, templateId);

        // THEN
        assertThat(result).isInstanceOf(EmailTemplateFoundFailure.class);
        verify(emailTemplateRepository).getEmailTemplateById(templateId);
        verify(userFetcher).fetch(userId);
        verify(emailService, never()).send(emailNotification);
    }

    @Test
    void shouldReturnAUserEmailAddressFailureIfEmailTemplateNotExists() {
        // GIVEN
        String userEmailAddress = "test@email.com";
        EmailNotification emailNotification = new EmailNotification(
                userEmailAddress,
                emailTemplate.getSubject(),
                emailTemplate.getBody()
        );
        when(emailTemplateRepository.getEmailTemplateById(templateId)).thenReturn(Optional.of(emailTemplate));
        when(userFetcher.fetch(userId)).thenReturn(Optional.empty());

        // WHEN
        SendEmailNotificationResponse result = useCase.execute(userId, templateId);

        // THEN
        assertThat(result).isInstanceOf(UserEmailAddressFailure.class);
        verify(emailTemplateRepository).getEmailTemplateById(templateId);
        verify(userFetcher).fetch(userId);
        verify(emailService, never()).send(emailNotification);
    }
  
}