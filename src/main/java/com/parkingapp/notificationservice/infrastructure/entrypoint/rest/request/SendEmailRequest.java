package com.parkingapp.notificationservice.infrastructure.entrypoint.rest.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendEmailRequest {

    @Schema(
            name = "user_id",
            description = "Notification recipient User ID",
            example = "9ba540cd-9190-4096-a20d-33035f437407"
    )
    @NotNull
    private UUID userId;

    @Schema(
            name = "template_id",
            description = "Template ID",
            example = "3fa85f64-5717-4562-b3fc-2c963f66afa6"
    )
    @NotNull
    private UUID templateId;

    @Schema(
            name = "params",
            description = "Additional information to add to the template",
            example = "{\"startDate\": \"2024-09-23 08:35:25.101794\", \"endDate\": \"2024-09-23 09:35:25.101794\"}"
    )
    private Map<String, Object> params;
}
