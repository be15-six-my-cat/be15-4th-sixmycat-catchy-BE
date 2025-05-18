package com.sixmycat.catchy.feature.block.query.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.block.query.dto.response.BlockResponse;
import com.sixmycat.catchy.feature.block.query.service.BlockQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blocks")
public class BlockQueryController {

    private final BlockQueryService blockQueryService;

    @GetMapping
    public ApiResponse<List<BlockResponse>> getBlockedUsers(@RequestHeader("X-USER-ID") Long blockerId) {
        return ApiResponse.success(blockQueryService.getBlockedUsers(blockerId));
    }
}
