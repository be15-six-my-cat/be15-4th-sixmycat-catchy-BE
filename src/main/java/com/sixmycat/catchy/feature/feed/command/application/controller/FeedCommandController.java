package com.sixmycat.catchy.feature.feed.command.application.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedCreateRequest;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedUpdateRequest;
import com.sixmycat.catchy.feature.feed.command.application.service.FeedCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedCommandController {

    private final FeedCommandService feedCommandService;

    @PostMapping
    public ApiResponse<Long> createFeed(@RequestBody FeedCreateRequest request,
                                        @RequestHeader("X-USER-ID") Long memberId) {
        Long feedId = feedCommandService.createFeed(request, memberId);
        return ApiResponse.success(feedId);
    }

    @PutMapping("/{id}")
    public ApiResponse<Long> updateFeed(
            @PathVariable Long id,
            @RequestBody FeedUpdateRequest request,
            @RequestHeader("X-USER-ID") Long memberId
    ) {
        feedCommandService.updateFeed(id, request, memberId);
        return ApiResponse.success(id);
    }

    @DeleteMapping("/id")
    public ApiResponse<Void> deleteFeed(@PathVariable Long feedId,
                                        @RequestHeader("X-USER-ID") Long memberId) {
        feedCommandService.deleteFeed(feedId, memberId);
        return ApiResponse.success(null);
    }
}