package com.parkingapp.notificationservice.infrastructure.email;

import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.parkingapp.notificationservice.domain.email.EmailNotification;
import com.parkingapp.notificationservice.domain.email.EmailService;
import com.parkingapp.notificationservice.domain.email.NotificationResult;
import com.parkingapp.notificationservice.infrastructure.config.EmailNotificationConfig;
import com.parkingapp.notificationservice.infrastructure.fixtures.initializers.testannotation.IntegrationTest;
import com.parkingapp.notificationservice.infrastructure.fixtures.initializers.testannotation.WithGreenMail;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static com.parkingapp.notificationservice.domain.email.NotificationResult.*;
import static org.assertj.core.api.Assertions.assertThat;

@WithGreenMail
@SpringBootTest(
        classes = {EmailNotificationConfig.class}
)
@IntegrationTest
public class JmsEmailServiceIntegrationTest {

    @Autowired
    private GreenMail greenMail;

    @Autowired
    private EmailService emailService;

    @BeforeEach
    void setUp() throws FolderException {
        greenMail.purgeEmailFromAllMailboxes();
    }

    String recipient = "test2@recipient.com";
    String subject = "Integration Test Subject";
    String body = "<b th:text=\"${name}\"> name</b>";
    String userName = "john";

    @Test
    void shouldSendEmail() throws Exception {
        // GIVEN
        EmailNotification emailNotification = new EmailNotification(
                recipient,
                subject,
                body,
                Map.of("name", userName)
        );

        // WHEN
        NotificationResult result = emailService.send(emailNotification);

        // THEN
        assertThat(result).isInstanceOf(SuccessNotification.class);
    }

    @Test
    void shouldReceiveAEmail() throws Exception {
        // GIVEN
        EmailNotification emailNotification = new EmailNotification(
                recipient,
                subject,
                body,
                Map.of("name", userName)
        );

        // WHEN
        NotificationResult result = emailService.send(emailNotification);

        // THEN
        assertThat(result).isInstanceOf(SuccessNotification.class);
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertThat(receivedMessages.length).isEqualTo(1);
    }

    @Test
    void emailBodyShouldHaveProvidedParams() throws Exception {
        // GIVEN
        EmailNotification emailNotification = new EmailNotification(
                recipient,
                subject,
                body,
                Map.of("name", userName)
        );

        // WHEN
        NotificationResult result = emailService.send(emailNotification);

        // THEN
        assertThat(result).isInstanceOf(SuccessNotification.class);
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        MimeMessage receivedMessage = receivedMessages[0];

        String receivedBody = GreenMailUtil.getBody(receivedMessage);
        assertThat(receivedBody).contains(userName);
    }

    @Test
    void emailShouldHaveProvidedSubject() throws Exception {
        // GIVEN
        EmailNotification emailNotification = new EmailNotification(
                recipient,
                subject,
                body,
                Map.of("name", userName)
        );

        // WHEN
        NotificationResult result = emailService.send(emailNotification);

        // THEN
        assertThat(result).isInstanceOf(SuccessNotification.class);
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        MimeMessage receivedMessage = receivedMessages[0];

        assertThat(subject).isEqualTo(receivedMessage.getSubject());
    }

    @Test
    void emailShouldBeSentToProvidedRecipient() throws Exception {
        // GIVEN
        EmailNotification emailNotification = new EmailNotification(
                recipient,
                subject,
                body,
                Map.of("name", userName)
        );

        // WHEN
        NotificationResult result = emailService.send(emailNotification);

        // THEN
        assertThat(result).isInstanceOf(SuccessNotification.class);
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();

        MimeMessage receivedMessage = receivedMessages[0];

        assertThat(recipient).isEqualTo(receivedMessage.getAllRecipients()[0].toString());
    }

    @Test
    void shouldReturnAFailureNotificationIfSendEmailFail() throws Exception {
        // GIVEN
        EmailNotification emailNotification = new EmailNotification(
                null,
                subject,
                body,
                Map.of("name", userName)
        );

        // WHEN
        NotificationResult result = emailService.send(emailNotification);

        // THEN
        assertThat(result).isInstanceOf(FailureNotification.class);
    }
}