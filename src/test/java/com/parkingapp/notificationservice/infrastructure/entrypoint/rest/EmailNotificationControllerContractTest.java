package com.parkingapp.notificationservice.infrastructure.entrypoint.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingapp.notificationservice.application.sendemailnotification.SendEmailNotificationUseCase;
import com.parkingapp.notificationservice.infrastructure.fixtures.initializers.testannotation.ContractTest;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.restassured.module.mockmvc.response.MockMvcResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static com.parkingapp.notificationservice.application.sendemailnotification.SendEmailNotificationResponse.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ContractTest
@WebMvcTest(controllers = EmailNotificationController.class)
class EmailNotificationControllerContractTest {

    @Autowired
    private WebApplicationContext context;

    @MockBean
    private SendEmailNotificationUseCase sendEmailNotificationUseCase;

    @Autowired
    private ObjectMapper objectMapper;

    private final UUID userId = UUID.randomUUID();
    private final UUID templateId = UUID.randomUUID();
    String requestBody = String.format(
            """
                {
                    "user_id": "%s",
                    "template_id": "%s",
                    "params": {}
                }
            """,
            userId,
            templateId
    );

    @Test
    public void shouldSentAnEmail() throws Exception {
        // GIVEN
        when(sendEmailNotificationUseCase.execute(userId, templateId)).thenReturn(new Successful());

        // WHEN
        MockMvcResponse response = whenARequestToSendAnEmailIsReceived(requestBody);

        // THEN
        response.then()
                .statusCode(HttpStatus.ACCEPTED.value());
        verify(sendEmailNotificationUseCase).execute(userId, templateId);
    }

    @Test
    public void shouldReturn400WhenBodyIsNotCorrect() {
        // GIVEN
        String incorrectRequestBody = String.format(
                """
                    {
                        "incorrect_field": "%s",
                        "template_id": "%s",
                        "params": {}
                    }
                """,
                userId,
                templateId
        );

        // WHEN
        MockMvcResponse response = whenARequestToSendAnEmailIsReceived(incorrectRequestBody);

        // THEN
        response.then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
        verify(sendEmailNotificationUseCase, never()).execute(userId, templateId);
    }

    @Test
    public void shouldReturn500WhenErrorOccurs() {
        // GIVEN
        doThrow(new RuntimeException("ops")).when(sendEmailNotificationUseCase).execute(userId, templateId);

        // WHEN
        MockMvcResponse response = whenARequestToSendAnEmailIsReceived(requestBody);

        // THEN
        response.then()
                .statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());

        verify(sendEmailNotificationUseCase).execute(userId, templateId);
    }

    private MockMvcResponse whenARequestToSendAnEmailIsReceived(String requestBody) {
        return RestAssuredMockMvc
                .given()
                .webAppContextSetup(context)
                .contentType(ContentType.JSON)
                .body(requestBody)
                .when()
                .post("/send-email");
    }
}