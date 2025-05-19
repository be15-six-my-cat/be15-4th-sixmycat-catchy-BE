package com.sixmycat.catchy.feature.jjure.query.service;

import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.feed.query.dto.response.FeedDetailResponse;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureDetailResponse;

public interface JjureQueryService {
    JjureDetailResponse getJjureDetail(Long jjureId, Long userId);

    PageResponse<JjureDetailResponse> getJjureList(Long userId, int page, int size);
}