package com.main.app.controller;

import com.main.app.domain.model.Notification;
import com.main.app.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/critical")
    public List<Notification> getCriticalNotifications() {
        return notificationService.getCriticalNotifications();
    }



    @PostMapping("/read/{notificationId}")
    public ResponseEntity<String> readNotification(@PathVariable Long notificationId) {
        try {
            notificationService.readNotification(notificationId);
            return ResponseEntity.ok("Notification marked as read.");
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
