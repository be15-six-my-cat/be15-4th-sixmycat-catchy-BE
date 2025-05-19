package com.sixmycat.catchy.feature.block.command.domain.repository;

import com.sixmycat.catchy.feature.block.command.domain.aggregate.Block;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockRepository extends JpaRepository<Block, Long> {
    boolean existsByBlockerIdAndBlockedId(Long blockerId, Long blockedId);
}
