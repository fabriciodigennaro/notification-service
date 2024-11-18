package com.parkingapp.notificationservice.infrastructure.config;

import com.parkingapp.notificationservice.domain.user.UserRepository;
import com.parkingapp.notificationservice.domain.user.UserService;
import com.parkingapp.notificationservice.infrastructure.user.CompositeUserFetcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FetcherConfig {

    @Bean
    public CompositeUserFetcher CompositeUserFetcher(UserRepository userRepository, UserService userService) {
       return new CompositeUserFetcher(userRepository, userService);
    }
}
