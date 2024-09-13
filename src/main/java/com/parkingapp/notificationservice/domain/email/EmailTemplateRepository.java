package com.parkingapp.notificationservice.domain.email;

import java.util.UUID;

public interface EmailTemplateRepository {
    EmailTemplate getEmailTemplateById(UUID templateId);
}
