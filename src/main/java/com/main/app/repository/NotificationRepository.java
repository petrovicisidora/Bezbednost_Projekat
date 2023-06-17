package com.main.app.repository;

import com.main.app.domain.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Notification findByEmail(String email);

    List<Notification> findByCriticalTrue();
}
