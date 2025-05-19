package com.sixmycat.catchy.feature.block.query.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
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
        List<BlockResponse> blockedUsers = blockQueryRepository.findBlockedUsers(blockerId);

        if (blockedUsers == null || blockedUsers.isEmpty()) {
            throw new BusinessException(ErrorCode.BLOCK_NOT_FOUND);
        }

        return blockedUsers;
    }
}
