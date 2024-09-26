package com.parkingapp.notificationservice.domain.exceptions;

import java.util.UUID;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(UUID userId) {
        super(String.format("User with id %s already exists", userId));
    }
}
