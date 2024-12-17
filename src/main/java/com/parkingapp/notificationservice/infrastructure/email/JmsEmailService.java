package com.parkingapp.notificationservice.infrastructure.email;

import com.parkingapp.notificationservice.domain.email.EmailNotification;
import com.parkingapp.notificationservice.domain.email.EmailService;
import com.parkingapp.notificationservice.domain.email.NotificationResult;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;

import static com.parkingapp.notificationservice.domain.email.NotificationResult.*;


public class JmsEmailService implements EmailService {

    private final JavaMailSender javaMailSender;
    private final String sender;
    private final TemplateEngine templateEngine;

    public JmsEmailService(JavaMailSender javaMailSender, String sender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.sender = sender;
        this.templateEngine = templateEngine;
    }

    @Override
    public NotificationResult send(EmailNotification emailNotification) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            Map<String, Object> params = emailNotification.getParams();

            Context context = new Context();
            context.setVariables(params);

            String htmlContent = templateEngine.process(emailNotification.getBody(), context);
            mimeMessageHelper.setTo(emailNotification.getRecipient());
            mimeMessageHelper.setSubject(emailNotification.getSubject());
            mimeMessageHelper.setFrom(this.sender);
            mimeMessageHelper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
            return new SuccessNotification();
        } catch (Exception e) {
            return new FailureNotification();
        }

    }
}
