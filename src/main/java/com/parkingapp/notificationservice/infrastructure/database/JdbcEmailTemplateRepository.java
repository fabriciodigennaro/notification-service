package com.parkingapp.notificationservice.infrastructure.database;

import com.parkingapp.notificationservice.domain.email.EmailTemplate;
import com.parkingapp.notificationservice.domain.email.EmailTemplateRepository;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class JdbcEmailTemplateRepository implements EmailTemplateRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcEmailTemplateRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public Optional<EmailTemplate> getEmailTemplateById(UUID templateId) {
        return namedParameterJdbcTemplate.query(
                """
                   SELECT * FROM email_templates
                   WHERE id = :templateId
                """,
                Map.of("templateId", templateId),
                new EmailTemplateRowMapper()
        )
                .stream().findFirst();
    }

    private static class EmailTemplateRowMapper implements RowMapper<EmailTemplate> {
        @Override
        public EmailTemplate mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new EmailTemplate(
                    UUID.fromString(rs.getString("id")),
                    rs.getString("subject"),
                    rs.getString("body")
            );
        }
    }
}
