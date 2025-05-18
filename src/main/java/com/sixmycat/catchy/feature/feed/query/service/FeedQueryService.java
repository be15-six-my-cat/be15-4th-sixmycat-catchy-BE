package com.sixmycat.catchy.feature.feed.query.service;

import com.sixmycat.catchy.feature.feed.query.dto.response.FeedDetailResponse;

public interface FeedQueryService {
    FeedDetailResponse getFeedDetail(Long feedId, Long userId);
}