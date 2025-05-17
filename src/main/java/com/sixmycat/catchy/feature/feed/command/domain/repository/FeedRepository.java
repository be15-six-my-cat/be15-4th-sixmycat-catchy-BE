package com.sixmycat.catchy.feature.feed.command.domain.repository;

import com.sixmycat.catchy.feature.feed.command.domain.aggregate.Feed;

public interface FeedRepository {
    Feed save(Feed feed);
}