package com.parkingapp.notificationservice.domain.user;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    Optional<User> getUserById(UUID userId);

    void saveUser(User user);
}
