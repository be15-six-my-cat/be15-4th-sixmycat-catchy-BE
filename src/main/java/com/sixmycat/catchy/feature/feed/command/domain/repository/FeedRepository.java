package com.sixmycat.catchy.feature.feed.command.domain.repository;

import com.sixmycat.catchy.feature.feed.command.domain.aggregate.Feed;

import java.util.Optional;

public interface FeedRepository {
    Feed save(Feed feed);

    Optional<Feed> findById(Long feedId);
}