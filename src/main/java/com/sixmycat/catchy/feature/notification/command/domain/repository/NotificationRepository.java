package com.sixmycat.catchy.feature.notification.command.domain.repository;

import com.sixmycat.catchy.feature.notification.command.domain.aggregate.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
