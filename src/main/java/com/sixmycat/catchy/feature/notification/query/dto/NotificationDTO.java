package com.sixmycat.catchy.feature.notification.query.dto;

import com.sixmycat.catchy.feature.notification.command.domain.aggregate.NotificationType;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class NotificationDTO {
    Long memberId;
    Long senderMemberId;
    String profileImage;
    String content;
    NotificationType type;
    Long relatedId;
    LocalDateTime createdAt;
}
