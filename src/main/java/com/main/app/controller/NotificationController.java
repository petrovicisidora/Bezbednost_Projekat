package com.main.app.controller;

import com.main.app.domain.model.Notification;
import com.main.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/critical")
    public List<Notification> getCriticalNotifications() {
        return notificationService.getCriticalNotifications();
    }


}
