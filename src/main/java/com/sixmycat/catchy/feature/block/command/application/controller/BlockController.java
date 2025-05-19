package com.sixmycat.catchy.feature.block.command.application.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.block.command.application.service.BlockService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/blocks")
public class BlockController {

    private final BlockService blockService;

    @PostMapping("/{blockedId}")
    public ApiResponse<Void> block(@RequestHeader("X-USER-ID") Long blockerId,
                                   @PathVariable Long blockedId) {
        blockService.blockUser(blockerId, blockedId);
        return ApiResponse.success(null);
    }

    @DeleteMapping("/{blockedId}")
    public ApiResponse<Void> unblock(@RequestHeader("X-USER-ID") Long blockerId,
                                     @PathVariable Long blockedId) {
        blockService.unblockUser(blockerId, blockedId);
        return ApiResponse.success(null);
    }
}
