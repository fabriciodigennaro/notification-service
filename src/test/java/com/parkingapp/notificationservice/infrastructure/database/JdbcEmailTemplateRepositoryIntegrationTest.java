package com.parkingapp.notificationservice.infrastructure.database;

import com.parkingapp.notificationservice.domain.email.EmailTemplate;
import com.parkingapp.notificationservice.domain.email.EmailTemplateRepository;
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
    private EmailTemplateRepository emailTemplateRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID templateId = UUID.randomUUID();
    UUID templateId2 = UUID.randomUUID();
    String subject = "dummy subject";
    String template = "dummy body";
    EmailTemplate emailTemplate = new EmailTemplate(
            templateId,
            subject,
            template
    );

    @Test
    void shouldGetAEmailTemplateById() {
        // GIVEN
        givenAnExistingEmailTemplate();

        // WHEN
        Optional<EmailTemplate> expectedTemplate = emailTemplateRepository.getEmailTemplateById(templateId);

        // THEN
        assertThat(expectedTemplate).isEqualTo(Optional.of(emailTemplate));
    }

    @Test
    void shouldNotFindAEmailTemplateWhenTemplateIdNotExists() {
        // GIVEN
        givenAnExistingEmailTemplate();

        // WHEN
        Optional<EmailTemplate> expectedTemplate = emailTemplateRepository.getEmailTemplateById(templateId2);

        // THEN
        assertThat(expectedTemplate).isEmpty();
    }

    private void givenAnExistingEmailTemplate() {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", templateId)
                .addValue("subject", subject)
                .addValue("template", template);

        namedParameterJdbcTemplate.update(
                "INSERT INTO email_templates(id, subject, template) VALUES (:id, :subject, :template);",
                params
        );
    }

}