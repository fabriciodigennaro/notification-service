package com.parkingapp.notificationservice.infrastructure.client.userservice;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.http.Fault;
import com.parkingapp.notificationservice.domain.user.User;
import com.parkingapp.notificationservice.infrastructure.config.ObjectMapperConfig;
import com.parkingapp.notificationservice.infrastructure.config.client.UserClientConfig;
import com.parkingapp.notificationservice.infrastructure.fixtures.initializers.testannotation.IntegrationTest;
import com.parkingapp.notificationservice.infrastructure.fixtures.initializers.testannotation.WithWireMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@WithWireMock
@SpringBootTest(
        classes = {UserClientConfig.class, ObjectMapperConfig.class}
)
@IntegrationTest
class UserClientIntegrationTest {
    @Autowired
    private WireMockServer wireMockServer;

    @Autowired
    private UserClient userClient;

    private final UUID userId = UUID.randomUUID();

    MappingBuilder baseResponse = get(urlPathEqualTo("/user-service"))
            .withQueryParam("userId", equalTo(userId.toString()));

    @BeforeEach
    public void setUp() { wireMockServer.resetAll(); }

    @Test
    public void shouldReturnAUserWhenResponseIsSuccessful() {
        // GIVEN
        String email = "dummy@email.com";
        wireMockServer.givenThat(
                get(urlPathEqualTo("/users/" + userId.toString()))
                        .willReturn(aResponse()
                                .withStatus(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(String.format("{\"id\": \"%s\", \"email\": \"%s\"}", userId.toString(), email))
                        )
        );

        // WHEN
        Optional<User> response = userClient.getUserById(userId);

        // THEN
        assertThat(response).isPresent();
        assertThat(response.get().getId()).isEqualTo(userId);
        assertThat(response.get().getEmail()).isEqualTo(email);
        wireMockServer.verify(getRequestedFor(urlPathEqualTo("/users/" + userId.toString())));

    }

    @Test
    public void shouldNotReturnAUserWhenResponseIsNotSuccessful() {
        // GIVEN
        wireMockServer.givenThat(
                baseResponse.willReturn(aResponse().withStatus(500))
        );

        // WHEN
        Optional<User> response = userClient.getUserById(userId);

        // THEN
        assertThat(response).isEmpty();
    }

    @Test
    public void shouldHandleIOException() {
        // GIVEN
        wireMockServer.stubFor(get(urlPathEqualTo("/users/" + userId.toString()))
                .willReturn(aResponse().withFault(Fault.CONNECTION_RESET_BY_PEER))
        );

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> {
            userClient.getUserById(userId);
        });
    }
}