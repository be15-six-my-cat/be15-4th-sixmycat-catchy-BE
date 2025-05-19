package com.sixmycat.catchy.feature.feed.command.domain.repository;

import com.sixmycat.catchy.feature.feed.command.domain.aggregate.FeedComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedCommentRepository extends JpaRepository<FeedComment, Long> {
    boolean existsById(Long id);
}