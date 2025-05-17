package com.sixmycat.catchy.feature.feed.command.domain.aggregate;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_id")
    private Feed feed;

    public static FeedImage create(Feed feed, String imageUrl) {
        FeedImage feedImage = new FeedImage();
        feedImage.feed = feed;
        feedImage.imageUrl = imageUrl;
        return feedImage;
    }
}