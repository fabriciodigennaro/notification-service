package com.parkingapp.notificationservice.domain.exceptions;

import java.util.UUID;

public class EmailTemplateNotFoundException extends RuntimeException {
    public EmailTemplateNotFoundException(UUID templateId) {
        super(String.format("Email template with id: %s not found.", templateId));
    }
}
