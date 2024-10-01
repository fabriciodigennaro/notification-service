package com.parkingapp.notificationservice.domain.user;

import java.util.Optional;
import java.util.UUID;

public interface UserService {
    Optional<User> getUserById(UUID userId);
}
