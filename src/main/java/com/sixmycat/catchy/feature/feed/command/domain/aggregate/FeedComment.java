package com.sixmycat.catchy.feature.feed.command.domain.aggregate;

import com.sixmycat.catchy.common.domain.TargetType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "UPDATE feed_comment SET deleted_at = NOW() WHERE comment_id = ?")
@Where(clause = "deleted_at IS NULL")
public class FeedComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    private Long memberId; // 기존 Member → Long

    private Long targetId;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_type", nullable = false)
    private TargetType targetType;

    private Long parentCommentId;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(nullable = false)
    private String content;

    public static FeedComment create(Long memberId, Long targetId, TargetType targetType, String content, Long parentCommentId) {
        FeedComment comment = new FeedComment();
        comment.memberId = memberId;
        comment.targetId = targetId;
        comment.targetType = targetType;
        comment.content = content;
        comment.parentCommentId = parentCommentId;
        return comment;
    }
}
