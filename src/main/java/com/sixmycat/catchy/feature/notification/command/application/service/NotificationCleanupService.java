package com.sixmycat.catchy.feature.notification.command.application.service;

import com.sixmycat.catchy.feature.notification.command.domain.repository.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationCleanupService {

    private final NotificationRepository notificationRepository;

    // 매일 새벽 2시에 실행
    @Transactional
    @Scheduled(cron = "0 0 2 * * *")
    public void deleteOldNotifications() {
        LocalDateTime thresholdDate = LocalDateTime.now().minusDays(30);
        notificationRepository.deleteNotificationsOlderThan(thresholdDate);
    }
}
