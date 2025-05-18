package com.sixmycat.catchy.feature.feed.command.application.service;

import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedCreateRequest;

public interface FeedCommandService {
    Long createFeed(FeedCreateRequest request, Long memberId);
}