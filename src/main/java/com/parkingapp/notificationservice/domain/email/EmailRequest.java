package com.parkingapp.notificationservice.domain.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailRequest {
    private UUID userId;
    private UUID templateId;
    private Map<String, Object> params;
}
