package com.parkingapp.notificationservice.infrastructure.config.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parkingapp.notificationservice.domain.user.UserService;
import com.parkingapp.notificationservice.infrastructure.client.userservice.UserApi;
import com.parkingapp.notificationservice.infrastructure.client.userservice.UserClient;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.net.URI;
import java.time.Duration;

@Configuration
public class UserClientConfig {

    @Value("${clients.user.url}")
    URI baseUrl;

    @Value("${clients.user.timeout.connect}")
    long connectTimeout;

    @Value("${clients.user.timeout.read}")
    long readTimeout;

    @Bean
    public UserApi userApi(ObjectMapper objectMapper) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofMillis(connectTimeout))
                .readTimeout(Duration.ofMillis(readTimeout))
                .build();
        return new Retrofit.Builder()
                .baseUrl(baseUrl.toString())
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build()
                .create(UserApi.class);
    }

    @Bean
    public UserService userService(UserApi userApi) {
        return new UserClient(userApi);
    }
}
