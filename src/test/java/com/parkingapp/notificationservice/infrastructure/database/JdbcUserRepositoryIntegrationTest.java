package com.parkingapp.notificationservice.infrastructure.database;

import com.parkingapp.notificationservice.domain.user.UserRepository;
import com.parkingapp.notificationservice.infrastructure.fixtures.initializers.testannotation.IntegrationTest;
import com.parkingapp.notificationservice.infrastructure.fixtures.initializers.testannotation.WithPostgreSql;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@WithPostgreSql
class JdbcUserRepositoryIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    UUID userId = UUID.randomUUID();
    UUID userId2 = UUID.randomUUID();
    String email = "dummy@email.com";

    @Test
    void shouldGetAUserEmailAddressById() {
        // GIVEN
        givenAnExistingUser();

        // WHEN
        Optional<String> expectedEmailAddress = userRepository.getUserEmailAddressByUserId(userId);

        // THEN
        assertThat(expectedEmailAddress).isEqualTo(Optional.of(email));
    }

    @Test
    void shouldNotFindAEmailAddressWhenUserIdNotExists() {
        // GIVEN
        givenAnExistingUser();

        // WHEN
        Optional<String> expectedEmailAddress = userRepository.getUserEmailAddressByUserId(userId2);

        // THEN
        assertThat(expectedEmailAddress).isEmpty();
    }

    private void givenAnExistingUser() {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", userId)
                .addValue("email", email);

        namedParameterJdbcTemplate.update(
                """
                        INSERT INTO users(id, email)
                        VALUES (:id, :email);
                        """,
                params
        );
    }
}