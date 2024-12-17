package com.parkingapp.notificationservice.domain.email;

import static com.parkingapp.notificationservice.domain.email.NotificationResult.*;

public abstract sealed class NotificationResult permits FailureNotification, SuccessNotification {
    public static final class SuccessNotification extends NotificationResult {}
    public static final class FailureNotification extends NotificationResult {}
}
