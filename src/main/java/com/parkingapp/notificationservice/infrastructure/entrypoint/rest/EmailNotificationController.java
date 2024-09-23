package com.parkingapp.notificationservice.infrastructure.entrypoint.rest;

import com.parkingapp.notificationservice.application.sendemailnotification.SendEmailNotificationUseCase;
import com.parkingapp.notificationservice.infrastructure.entrypoint.rest.request.SendEmailRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
@Tag(name = "Email", description = "All about emails")
public class EmailNotificationController {
    private final SendEmailNotificationUseCase sendEmailNotificationUseCase;

    EmailNotificationController(SendEmailNotificationUseCase sendEmailNotificationUseCase) {
        this.sendEmailNotificationUseCase = sendEmailNotificationUseCase;
    }

    @Operation(summary = "Send an email notification")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "202",
                    description = "Accepted",
                    content = @Content(schema = @Schema(hidden = true))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    }
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error",
                    content = {
                            @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorResponse.class)
                            )
                    }
            )
    })
    @PostMapping("/create")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void sendEmail(@RequestBody @Valid SendEmailRequest request) {
        sendEmailNotificationUseCase.execute(request.getUserId(), request.getTemplateId());
    }
}
