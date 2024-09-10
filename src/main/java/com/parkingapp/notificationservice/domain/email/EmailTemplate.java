package com.parkingapp.notificationservice.domain.email;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class EmailTemplate {
    private UUID templateId;
    private String subject;
    private String body;
}
