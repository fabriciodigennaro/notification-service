package com.parkingapp.notificationservice.infrastructure.client.userservice;

import com.parkingapp.notificationservice.domain.user.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.UUID;

public interface UserApi {
    @GET("users/{userId}")
    Call<User> getUserById(@Path("userId") UUID userId);
}
