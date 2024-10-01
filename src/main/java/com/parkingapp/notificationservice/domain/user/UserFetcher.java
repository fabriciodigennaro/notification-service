package com.parkingapp.notificationservice.domain.user;

import java.util.Optional;
import java.util.UUID;

public interface UserFetcher {
    Optional<User> fetch(UUID userId);
}
