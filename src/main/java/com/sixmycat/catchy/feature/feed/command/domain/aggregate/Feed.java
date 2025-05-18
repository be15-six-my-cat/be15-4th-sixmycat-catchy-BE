package com.sixmycat.catchy.feature.feed.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String content;

    private Long memberId;

    private String musicUrl;

    @OneToMany(mappedBy = "feed", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeedImage> feedImages = new ArrayList<>();

    // 정적 팩토리 메서드
    public static Feed create(String content, Long memberId, String musicUrl, List<String> imageUrls) {
        Feed feed = new Feed();
        feed.content = content;
        feed.memberId = memberId;
        feed.musicUrl = musicUrl;

        for (int i = 0; i < imageUrls.size(); i++) {
            String imageUrl = imageUrls.get(i);
            feed.feedImages.add(FeedImage.create(feed, imageUrl, i)); // 순서 정보 추가
        }

        return feed;
    }
}