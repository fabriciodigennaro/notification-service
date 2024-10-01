package com.parkingapp.notificationservice.infrastructure.database;

import com.parkingapp.notificationservice.domain.user.User;
import com.parkingapp.notificationservice.domain.user.UserRepository;
import com.parkingapp.notificationservice.infrastructure.fixtures.initializers.testannotation.IntegrationTest;
import com.parkingapp.notificationservice.infrastructure.fixtures.initializers.testannotation.WithPostgreSql;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.jdbc.JdbcTestUtils;

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
    String email2 = "dummy2@email.com";
    User user = new User(userId, email);

    @BeforeEach
    void setUp() {
        JdbcTestUtils.deleteFromTables(
                namedParameterJdbcTemplate.getJdbcTemplate(),
                "users"
        );
    }

    @Test
    void shouldGetAUserById() {
        // GIVEN
        userRepository.saveUser(user);

        // WHEN
        Optional<User> expectedUser = userRepository.getUserById(userId);

        // THEN
        assertThat(expectedUser).isEqualTo(Optional.of(user));
    }

    @Test
    void shouldNotFindAUserWhenUserIdNotExists() {
        // GIVEN
        userRepository.saveUser(user);

        // WHEN
        Optional<User> expectedUser = userRepository.getUserById(userId2);

        // THEN
        assertThat(expectedUser).isEmpty();
    }

    @Test
    void shouldSaveAUser() {
        // GIVEN
        userRepository.saveUser(user);

        // THEN
        Optional<User> result = userRepository.getUserById(user.getId());
        assertThat(result).isEqualTo(Optional.of(user));
    }

    @Test
    void shouldUpdateAUser() {
        // GIVEN
        userRepository.saveUser(user);
        User userWithNewEmail = new User(userId, email2);

        // WHEN
        userRepository.saveUser(userWithNewEmail);
        Optional<User> userUpdated = userRepository.getUserById(userWithNewEmail.getId());

        // THEN
        assertThat(userUpdated).isPresent();
        assertThat(userWithNewEmail).isEqualTo(userUpdated.get());
        assertThat(user.getEmail()).isNotEqualTo(userWithNewEmail.getEmail());
        assertThat(user.getId()).isEqualTo(userWithNewEmail.getId());
    }
}