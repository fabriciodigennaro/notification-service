package com.parkingapp.notificationservice.infrastructure.database;

import com.parkingapp.notificationservice.domain.email.EmailTemplate;
import com.parkingapp.notificationservice.infrastructure.fixtures.initializers.testannotation.IntegrationTest;
import com.parkingapp.notificationservice.infrastructure.fixtures.initializers.testannotation.WithPostgreSql;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@WithPostgreSql
class JdbcEmailTemplateRepositoryIntegrationTest {

    @Autowired
    JdbcEmailTemplateRepository jdbcEmailTemplateRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID templateId = UUID.randomUUID();
    UUID templateId2 = UUID.randomUUID();
    String subject = "test subject";
    String body = "test body";
    private final EmailTemplate emailTemplate = new EmailTemplate(
            templateId,
            subject,
            body
    );

    @Test
    void shouldReturnAEmailTemplateByTemplateId() {
        // GIVEN
        EmailTemplate emailTemplate2 = new EmailTemplate(
                templateId2,
                subject,
                body
        );

        givenExistingEmailTemplate(emailTemplate);
        givenExistingEmailTemplate(emailTemplate2);

        // WHEN
        Optional<EmailTemplate> expectedTemplate = jdbcEmailTemplateRepository.getEmailTemplateById(templateId);

        // THEN
        assertThat(expectedTemplate).isEqualTo(Optional.of(emailTemplate));
    }

    @Test
    void shouldReturnAExceptionWhenEmailTemplateIsNotExist() {
        // GIVEN
        givenExistingEmailTemplate(emailTemplate);
        UUID nonExistentTemplateId = UUID.randomUUID();

        // WHEN
        Optional<EmailTemplate> expectedTemplate = jdbcEmailTemplateRepository.getEmailTemplateById(nonExistentTemplateId);

        // THEN
        assertThat(expectedTemplate).isEmpty();
    }

    private void givenExistingEmailTemplate(EmailTemplate emailTemplate) {
        MapSqlParameterSource templateParams = new MapSqlParameterSource()
                .addValue("id", emailTemplate.getTemplateId())
                .addValue("subject", emailTemplate.getSubject())
                .addValue("body", emailTemplate.getBody());

        namedParameterJdbcTemplate.update(
                """
                INSERT INTO email_templates(id, subject, body)
                VALUES (:id, :subject, :body)
                """,
                templateParams
        );
    }

}