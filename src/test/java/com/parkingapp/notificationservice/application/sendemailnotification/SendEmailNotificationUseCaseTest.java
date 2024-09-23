package com.parkingapp.notificationservice.application.sendemailnotification;

import com.parkingapp.notificationservice.domain.email.EmailNotification;
import com.parkingapp.notificationservice.domain.email.EmailService;
import com.parkingapp.notificationservice.domain.email.EmailTemplate;
import com.parkingapp.notificationservice.domain.email.EmailTemplateRepository;
import com.parkingapp.notificationservice.domain.user.UserRepository;
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
    private final UserRepository userRepository = mock(UserRepository.class);
    private final EmailService emailService = mock(EmailService.class);
    private final SendEmailNotificationUseCase useCase = new SendEmailNotificationUseCase(emailTemplateRepository, userRepository, emailService);

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
        String userEmailAddress = "test@email.com";
        EmailNotification emailNotification = new EmailNotification(
                userEmailAddress,
                emailTemplate.getSubject(),
                emailTemplate.getBody()
        );
        when(emailTemplateRepository.getEmailTemplateById(templateId)).thenReturn(Optional.of(emailTemplate));
        when(userRepository.getUserEmailAddressByUserId(userId)).thenReturn(Optional.of(userEmailAddress));

        // WHEN
        SendEmailNotificationResponse result = useCase.execute(userId, templateId);

        // THEN
        assertThat(result).isInstanceOf(Successful.class);
        verify(emailTemplateRepository).getEmailTemplateById(templateId);
        verify(userRepository).getUserEmailAddressByUserId(userId);
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
        when(userRepository.getUserEmailAddressByUserId(userId)).thenReturn(Optional.of(userEmailAddress));

        // WHEN
        SendEmailNotificationResponse result = useCase.execute(userId, templateId);

        // THEN
        assertThat(result).isInstanceOf(EmailTemplateFoundFailure.class);
        verify(emailTemplateRepository).getEmailTemplateById(templateId);
        verify(userRepository).getUserEmailAddressByUserId(userId);
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
        when(userRepository.getUserEmailAddressByUserId(userId)).thenReturn(Optional.empty());

        // WHEN
        SendEmailNotificationResponse result = useCase.execute(userId, templateId);

        // THEN
        assertThat(result).isInstanceOf(UserEmailAddressFailure.class);
        verify(emailTemplateRepository).getEmailTemplateById(templateId);
        verify(userRepository).getUserEmailAddressByUserId(userId);
        verify(emailService, never()).send(emailNotification);
    }
  
}