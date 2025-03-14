package com.parkingapp.notificationservice.domain.email;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class EmailNotification {
    private String recipient;
    private String subject;
    private String body;
    private Map<String, Object> params;
}
