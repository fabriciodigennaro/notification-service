package com.parkingapp.notificationservice.infrastructure.fixtures.initializers;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ContextClosedEvent;

public class GreenMailContainer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        GreenMail greenMail = new GreenMail(new ServerSetup(3025, null, "smtp"));
        greenMail.setUser("test@test.com", "test@test.com", "1234");
        greenMail.start();

        applicationContext.addApplicationListener(event -> {
            if (event instanceof ContextClosedEvent) {
                greenMail.stop();
            }
        });

        applicationContext.getBeanFactory().registerSingleton("greenMail", greenMail);

        TestPropertyValues.of(
                        "spring.mail.host=localhost",
                        "spring.mail.port=" + greenMail.getSmtp().getPort()
                )
                .applyTo(applicationContext);
    }
}
