package com.parkingapp.notificationservice.infrastructure.email;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.parkingapp.notificationservice.domain.email.EmailNotification;
import com.parkingapp.notificationservice.domain.email.EmailService;
import com.parkingapp.notificationservice.infrastructure.config.EmailNotificationConfig;
import com.parkingapp.notificationservice.infrastructure.fixtures.initializers.testannotation.IntegrationTest;
import com.parkingapp.notificationservice.infrastructure.fixtures.initializers.testannotation.WithGreenMail;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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

    @AfterEach
    void tearDown() {
        greenMail.reset();
    }

    @Test
    void testSendEmail() throws Exception {
        // GIVEN
        String recipient = "test2@recipient.com";
        String subject = "Integration Test Subject";
        String body = "This is the body of the integration test";
        EmailNotification emailNotification = new EmailNotification(
                recipient,
                subject,
                body
        );

        // WHEN
        emailService.send(emailNotification);

        // THEN
        MimeMessage[] receivedMessages = greenMail.getReceivedMessages();
        assertThat(receivedMessages.length).isEqualTo(1);

        MimeMessage receivedMessage = receivedMessages[0];
        assertThat(subject).isEqualTo(receivedMessage.getSubject());
        assertThat(recipient).isEqualTo(receivedMessage.getAllRecipients()[0].toString());
        assertThat(body).isEqualTo(GreenMailUtil.getBody(receivedMessage));
    }
}