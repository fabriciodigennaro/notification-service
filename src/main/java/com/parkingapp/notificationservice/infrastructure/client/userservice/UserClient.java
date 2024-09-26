package com.parkingapp.notificationservice.infrastructure.client.userservice;

import com.parkingapp.notificationservice.domain.user.User;

import com.parkingapp.notificationservice.domain.user.UserService;
import retrofit2.Response;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class UserClient implements UserService {
    private final UserApi userApi;

    public UserClient(UserApi userApi) {
        this.userApi = userApi;
    }


    @Override
    public Optional<User> getUserById(UUID userId) {
        try {
            Response<User> response = userApi.getUserById(userId).execute();

            if (response.isSuccessful() && response.body() != null) {
                return Optional.of(response.body());
            } else {
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to fetch user", e);
        }
    }
}
