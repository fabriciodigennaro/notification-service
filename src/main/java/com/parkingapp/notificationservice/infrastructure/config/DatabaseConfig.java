package com.parkingapp.notificationservice.infrastructure.config;

import com.parkingapp.notificationservice.domain.email.EmailTemplateRepository;
import com.parkingapp.notificationservice.infrastructure.database.JdbcEmailTemplateRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@Configuration
public class DatabaseConfig {

    @Bean
    public EmailTemplateRepository emailTemplateRepository(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate
    ) {
        return new JdbcEmailTemplateRepository(namedParameterJdbcTemplate);
    }
}
