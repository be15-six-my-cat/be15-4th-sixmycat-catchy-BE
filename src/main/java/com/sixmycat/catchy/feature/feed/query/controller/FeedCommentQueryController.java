package com.sixmycat.catchy.feature.feed.query.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.feed.query.dto.response.FeedCommentResponse;
import com.sixmycat.catchy.feature.feed.query.service.FeedCommentQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds")
public class FeedCommentQueryController {

    private final FeedCommentQueryService commentQueryService;

    @GetMapping("/{feedId}/comments")
    public ApiResponse<List<FeedCommentResponse>> getComments(@PathVariable Long feedId) {
        return ApiResponse.success(commentQueryService.getComments(feedId));
    }
}

