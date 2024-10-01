package com.parkingapp.notificationservice.infrastructure.user;

import com.parkingapp.notificationservice.domain.user.User;
import com.parkingapp.notificationservice.domain.user.UserRepository;
import com.parkingapp.notificationservice.domain.user.UserService;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class CompositeUserFetcherTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserService userService = mock(UserService.class);
    private final CompositeUserFetcher compositeUserFetcher = new CompositeUserFetcher(userRepository, userService);

    UUID userId = UUID.randomUUID();
    String userEmail = "dummy@email.com";
    User user = new User(userId, userEmail);

    @Test
    void shouldFetchUserFromUserRepository() {
        // GIVEN
        when(userRepository.getUserById(userId)).thenReturn(Optional.of(user));

        // WHEN
        Optional<User> userFetched = compositeUserFetcher.fetch(userId);

        // THEN
        assertThat(userFetched).isPresent();
        assertThat(userFetched).isEqualTo(Optional.of(user));
        verify(userService, times(0)).getUserById(userId);
    }

    @Test
    void shouldFetchUserFromUserService() {
        // GIVEN
        when(userRepository.getUserById(userId)).thenReturn(Optional.empty());
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        // WHEN
        Optional<User> userFetched = compositeUserFetcher.fetch(userId);

        // THEN
        assertThat(userFetched).isPresent();
        assertThat(userFetched).isEqualTo(Optional.of(user));
        verify(userRepository, times(1)).getUserById(userId);
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void shouldSaveAUserFromUserServiceToLocalDb() {
        // GIVEN
        when(userRepository.getUserById(userId)).thenReturn(Optional.empty());
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        // WHEN
        Optional<User> userFetched = compositeUserFetcher.fetch(userId);

        // THEN
        assertThat(userFetched).isPresent();
        assertThat(userFetched).isEqualTo(Optional.of(user));
        verify(userRepository, times(1)).saveUser(user);
    }

    @Test
    void shouldReturnAnEmptyOptionalIfUserNotExist() {
        // GIVEN
        when(userRepository.getUserById(userId)).thenReturn(Optional.empty());
        when(userService.getUserById(userId)).thenReturn(Optional.empty());

        // WHEN
        Optional<User> userFetched = compositeUserFetcher.fetch(userId);

        // THEN
        assertThat(userFetched).isEmpty();
        verify(userRepository, times(1)).getUserById(userId);
        verify(userService, times(1)).getUserById(userId);
    }

    @Test
    void shouldReturnEmptyOptionalWhenFetchingUserFails() {
        // GIVEN
        when(userRepository.getUserById(userId)).thenThrow(new RuntimeException("Database failure"));

        // WHEN
        Optional<User> userFetcherResult = compositeUserFetcher.fetch(userId);

        // THEN
        assertThat(userFetcherResult).isEmpty();
        verify(userService, times(0)).getUserById(userId);
    }
}