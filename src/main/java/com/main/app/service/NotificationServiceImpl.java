package com.main.app.service;

import com.main.app.domain.model.Notification;
import com.main.app.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService{

    @Autowired
    NotificationRepository notificationRepository;

    @Override
    public void createNotification(String message, String email, LocalDateTime time, int count, Boolean critical) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setEmail(email);
        notification.setTime(time);
        notification.setCount(count);
        notification.setCritical(critical);

        notificationRepository.save(notification);
    }

    @Override
    public List<Notification> getCriticalNotifications(){
        return notificationRepository.findByCriticalTrue();
    }
}
