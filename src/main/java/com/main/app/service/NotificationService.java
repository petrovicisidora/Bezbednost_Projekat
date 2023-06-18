package com.main.app.service;

import com.main.app.domain.model.Notification;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationService {
    void createNotification(String message,String email, LocalDateTime time, int count, Boolean critical);

    void readNotification(Long notificationId);

    List<Notification> getCriticalNotifications();
}
