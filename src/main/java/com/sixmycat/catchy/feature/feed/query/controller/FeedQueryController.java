package com.sixmycat.catchy.feature.feed.query.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.feed.query.dto.response.FeedDetailResponse;
import com.sixmycat.catchy.feature.feed.query.dto.response.FeedSummaryResponse;
import com.sixmycat.catchy.feature.feed.query.service.FeedQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PageResponse<FeedSummaryResponse>>> getMyFeeds(
            @RequestHeader(value = "X-USER-ID", required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size
    ) {
        PageResponse<FeedSummaryResponse> result = feedQueryService.getMyFeeds(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<FeedDetailResponse>>> getFeeds(
            @RequestHeader(value = "X-USER-ID", required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        PageResponse<FeedDetailResponse> response = feedQueryService.getFeedList(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/likes")
    public ResponseEntity<ApiResponse<PageResponse<FeedSummaryResponse>>> getLikedFeeds(
            @RequestHeader(value = "X-USER-ID", required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        PageResponse<FeedSummaryResponse> response = feedQueryService.getLikedFeedList(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}