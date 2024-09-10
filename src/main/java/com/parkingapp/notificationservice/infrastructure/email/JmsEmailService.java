package com.parkingapp.notificationservice.infrastructure.email;

import com.parkingapp.notificationservice.domain.email.EmailNotification;
import com.parkingapp.notificationservice.domain.email.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


public class JmsEmailService implements EmailService {

    private final JavaMailSender javaMailSender;

    private final String sender;

    public JmsEmailService(JavaMailSender javaMailSender, String sender) {
        this.javaMailSender = javaMailSender;
        this.sender = sender;
    }

    @Override
    public void send(EmailNotification emailNotification) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");

        try {
            mimeMessageHelper.setTo(emailNotification.getRecipient());
            mimeMessageHelper.setSubject(emailNotification.getSubject());
            mimeMessageHelper.setFrom(this.sender);
            mimeMessageHelper.setText(emailNotification.getBody());

            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
