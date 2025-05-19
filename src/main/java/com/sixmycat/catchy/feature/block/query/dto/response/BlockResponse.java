package com.sixmycat.catchy.feature.block.query.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BlockResponse {
    private Long blockedId;
    private String blockedNickname;
    private LocalDateTime blockedAt;
}