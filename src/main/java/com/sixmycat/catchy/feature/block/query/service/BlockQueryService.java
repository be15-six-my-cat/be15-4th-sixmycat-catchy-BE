package com.sixmycat.catchy.feature.block.query.service;

import com.sixmycat.catchy.feature.block.query.dto.response.BlockResponse;

import java.util.List;

public interface BlockQueryService {
    List<BlockResponse> getBlockedUsers(Long blockerId);
}
