package com.parkingapp.notificationservice.infrastructure.user;

import com.parkingapp.notificationservice.domain.user.User;
import com.parkingapp.notificationservice.domain.user.UserFetcher;
import com.parkingapp.notificationservice.domain.user.UserRepository;
import com.parkingapp.notificationservice.domain.user.UserService;

import java.util.Optional;
import java.util.UUID;

public class CompositeUserFetcher implements UserFetcher {
    private final UserRepository userRepository;
    private final UserService userService;

    public CompositeUserFetcher(UserRepository userRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public Optional<User> fetch(UUID userId) {
        try {
            Optional<User> userFromLocalDatabase = userRepository.getUserById(userId);
            if (userFromLocalDatabase.isPresent()) {
                return userFromLocalDatabase;
            }

            Optional<User> userFromExternalService = userService.getUserById(userId);
            if (userFromExternalService.isPresent()) {
                userRepository.saveUser(userFromExternalService.get());
                return userFromExternalService;
            }

            return Optional.empty();
        } catch (Throwable e) {
            return Optional.empty();
        }
    }
}
