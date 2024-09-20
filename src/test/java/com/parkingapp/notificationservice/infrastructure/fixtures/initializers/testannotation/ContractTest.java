package com.parkingapp.notificationservice.infrastructure.fixtures.initializers.testannotation;

import com.parkingapp.notificationservice.infrastructure.config.ObjectMapperConfig;
import org.junit.jupiter.api.Tag;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Tag("contract")
@ActiveProfiles("test")
@Import({ObjectMapperConfig.class})
public @interface ContractTest {
}
