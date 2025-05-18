package com.sixmycat.catchy.feature.feed.command.application.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedCommentCreateRequest;
import com.sixmycat.catchy.feature.feed.command.application.service.FeedCommentCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/feeds/comments")
public class FeedCommentCommandController {

    private final FeedCommentCommandService commentService;

    @PostMapping
    public ApiResponse<Long> createComment(@RequestBody FeedCommentCreateRequest request,
                                           @RequestHeader("X-USER-ID") Long memberId) {
        Long id = commentService.createComment(request, memberId);
        return ApiResponse.success(id);
    }
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteComment(@PathVariable("id") Long commentId,
                                           @RequestHeader("X-USER-ID") Long memberId) {
        commentService.deleteComment(commentId, memberId);
        return ApiResponse.success(null);
    }
}
