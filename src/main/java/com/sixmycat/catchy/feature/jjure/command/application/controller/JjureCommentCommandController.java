package com.sixmycat.catchy.feature.jjure.command.application.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureCommentCreateRequest;
import com.sixmycat.catchy.feature.jjure.command.application.service.JjureCommentCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jjure/comments")
public class JjureCommentCommandController {

    private final JjureCommentCommandService commentService;

    @PostMapping
    public ApiResponse<Long> createComment(@RequestBody JjureCommentCreateRequest request,
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
