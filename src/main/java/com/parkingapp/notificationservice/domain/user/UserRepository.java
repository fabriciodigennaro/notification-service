package com.parkingapp.notificationservice.domain.user;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    public Optional<String> getUserEmailAddressByUserId(UUID userId);
}
