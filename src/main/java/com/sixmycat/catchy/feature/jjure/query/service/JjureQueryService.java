package com.sixmycat.catchy.feature.jjure.query.service;

import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureDetailResponse;

public interface JjureQueryService {
    JjureDetailResponse getJjureDetail(Long jjureId, Long userId);
}