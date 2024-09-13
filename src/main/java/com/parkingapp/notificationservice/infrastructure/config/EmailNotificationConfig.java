package com.parkingapp.notificationservice.infrastructure.config;

import com.parkingapp.notificationservice.domain.email.EmailService;
import com.parkingapp.notificationservice.infrastructure.email.JmsEmailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class EmailNotificationConfig {

    @Bean
    public EmailService emailService(
            JavaMailSender javaMailSender,
            @Value("${spring.email.sender-email}") String sender
    ) {
        return new JmsEmailService(javaMailSender, sender);
    }

    @Bean
    public JavaMailSender getJavaMailSender(
            @Value("${spring.email.host}") String host,
            @Value("${spring.email.port}") int port,
            @Value("${spring.email.sender-email}") String user,
            @Value("${spring.email.password}") String password,
            @Value("${spring.email.properties.mail.smtp.auth}") String smtpAuth,
            @Value("${spring.email.properties.mail.smtp.starttls.enable}") String enableStartTls
    ) {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost(host);
        mailSender.setPort(port);
        mailSender.setUsername(user);
        mailSender.setPassword(password);

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", smtpAuth);
        props.put("mail.smtp.starttls.enable", enableStartTls);
        return mailSender;
    }
}
