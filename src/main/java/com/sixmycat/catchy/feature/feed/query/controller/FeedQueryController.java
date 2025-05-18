package com.sixmycat.catchy.feature.feed.query.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.feed.query.dto.response.FeedDetailResponse;
import com.sixmycat.catchy.feature.feed.query.service.FeedQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/feeds")
@RequiredArgsConstructor
public class FeedQueryController {

    private final FeedQueryService feedQueryService;

    @GetMapping("/{feedId}")
    public ApiResponse<FeedDetailResponse> getFeedDetail(
            @PathVariable Long feedId,
            @RequestHeader(value = "X-USER-ID", required = false) Long userId
    ) {
        FeedDetailResponse response = feedQueryService.getFeedDetail(feedId, userId);
        return ApiResponse.success(response);
    }
}