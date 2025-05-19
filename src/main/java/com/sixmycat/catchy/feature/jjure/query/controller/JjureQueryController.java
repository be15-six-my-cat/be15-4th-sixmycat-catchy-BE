package com.sixmycat.catchy.feature.jjure.query.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureDetailResponse;
import com.sixmycat.catchy.feature.jjure.query.service.JjureQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/jjures")
@RequiredArgsConstructor
public class JjureQueryController {

    private final JjureQueryService jjureQueryService;

    @GetMapping("/{jjureId}")
    public ResponseEntity<ApiResponse<JjureDetailResponse>> getJjureDetail(
            @PathVariable Long jjureId,
            @RequestHeader(value = "X-USER-ID", required = false) Long userId
    ) {
        JjureDetailResponse response = jjureQueryService.getJjureDetail(jjureId, userId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}