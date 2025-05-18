package com.sixmycat.catchy.feature.feed.query.service;

import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.feed.query.dto.response.FeedDetailResponse;
import com.sixmycat.catchy.feature.feed.query.dto.response.FeedSummaryResponse;

public interface FeedQueryService {
    FeedDetailResponse getFeedDetail(Long feedId, Long userId);
    PageResponse<FeedSummaryResponse> getMyFeeds(Long memberId, int page, int size);
}