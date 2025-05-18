package com.sixmycat.catchy.feature.block.query.service;

import com.sixmycat.catchy.feature.block.query.dto.response.BlockResponse;
import com.sixmycat.catchy.feature.block.query.mapper.BlockQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BlockQueryServiceImpl implements BlockQueryService {

    private final BlockQueryRepository blockQueryRepository;

    @Override
    public List<BlockResponse> getBlockedUsers(Long blockerId) {
        return blockQueryRepository.findBlockedUsers(blockerId);
    }
}
