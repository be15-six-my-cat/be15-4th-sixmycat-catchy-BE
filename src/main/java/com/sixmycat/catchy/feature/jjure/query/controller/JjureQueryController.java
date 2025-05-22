package com.sixmycat.catchy.feature.jjure.query.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureDetailResponse;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureSummaryResponse;
import com.sixmycat.catchy.feature.jjure.query.service.JjureQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/jjures")
@RequiredArgsConstructor
public class JjureQueryController {

    private final JjureQueryService jjureQueryService;

    /* 쮸르 상세 조회 */
    @GetMapping("/{jjureId}")
    public ResponseEntity<ApiResponse<JjureDetailResponse>> getJjureDetail(
            @PathVariable Long jjureId,
            @AuthenticationPrincipal String userId
    ) {
        JjureDetailResponse response = jjureQueryService.getJjureDetail(jjureId, Long.parseLong(userId));
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 쮸르 목록 조회 */
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<JjureDetailResponse>>> getJjures(
            @AuthenticationPrincipal String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        PageResponse<JjureDetailResponse> response = jjureQueryService.getJjureList(Long.parseLong(userId), page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/likes")
    public ResponseEntity<ApiResponse<PageResponse<JjureSummaryResponse>>> getLikedJjures(
            @RequestHeader(value = "X-USER-ID", required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        PageResponse<JjureSummaryResponse> response = jjureQueryService.getLikedJjureList(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<PageResponse<JjureSummaryResponse>>> getMyJjures(
            @RequestHeader(value = "X-USER-ID", required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        PageResponse<JjureSummaryResponse> response = jjureQueryService.getMyJjureList(userId, page, size);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}