package com.sixmycat.catchy.feature.jjure.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "jjure")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Jjure {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String caption;

    private String thumbnail_url;

    private String fileKey;

    private String musicUrl;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt;

    public void update(String caption, String fileKey, String thumbnail_url) {
        this.caption = caption;
        this.fileKey = fileKey;
        this.updatedAt = LocalDateTime.now();
        this.thumbnail_url = thumbnail_url;
    }

    public void markAsDeleted() {
        this.deletedAt = LocalDateTime.now();
    }
}
