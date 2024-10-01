package com.parkingapp.notificationservice.infrastructure.config;

import com.parkingapp.notificationservice.domain.email.EmailTemplateRepository;
import com.parkingapp.notificationservice.domain.user.UserRepository;
import com.parkingapp.notificationservice.infrastructure.database.JdbcEmailTemplateRepository;
import com.parkingapp.notificationservice.infrastructure.database.JdbcUserRepository;
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

    @Bean
    public UserRepository userRepository(
            NamedParameterJdbcTemplate namedParameterJdbcTemplate
    ) {
        return new JdbcUserRepository(namedParameterJdbcTemplate);
    }
}
