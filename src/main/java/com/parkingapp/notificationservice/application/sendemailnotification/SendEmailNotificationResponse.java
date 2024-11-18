package com.parkingapp.notificationservice.application.sendemailnotification;

import static com.parkingapp.notificationservice.application.sendemailnotification.SendEmailNotificationResponse.*;

public abstract sealed class SendEmailNotificationResponse permits
        Successful, Failure, EmailTemplateFoundFailure, UserEmailAddressFailure {

    public static final class Successful extends SendEmailNotificationResponse {}

    public static final class Failure extends SendEmailNotificationResponse {}

    public static final class EmailTemplateFoundFailure extends SendEmailNotificationResponse {}

    public static final class UserEmailAddressFailure extends SendEmailNotificationResponse {}
}

