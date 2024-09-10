package com.parkingapp.notificationservice.infrastructure.config;

import com.parkingapp.notificationservice.domain.email.EmailService;
import com.parkingapp.notificationservice.infrastructure.email.JmsEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class EmailNotificationConfig {

    @Bean
    public EmailService emailService(
            JavaMailSender javaMailSender,
            @Value("${spring.email.sender-email}") String sender
    ) {
        return new JmsEmailService(javaMailSender, sender);
    }
}
