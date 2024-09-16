package com.parkingapp.notificationservice.domain.email;

import java.util.Optional;
import java.util.UUID;

public interface EmailTemplateRepository {
    Optional<EmailTemplate> getEmailTemplateById(UUID templateId);
}
