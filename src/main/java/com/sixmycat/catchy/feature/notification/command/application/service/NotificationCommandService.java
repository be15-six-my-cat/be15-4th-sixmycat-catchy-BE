package com.sixmycat.catchy.feature.notification.command.application.service;

import com.sixmycat.catchy.feature.notification.command.domain.repository.NotificationRepository;
import com.sixmycat.catchy.feature.notification.command.infrastructure.repository.SseEmitterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Service
@RequiredArgsConstructor
public class NotificationCommandService {
    private final NotificationRepository notificationRepository;
    private final SseEmitterRepository sseEmitterRepository;

    public SseEmitter subscribe(Long memberId, String lastEventId) {
        return null;
    }
}
