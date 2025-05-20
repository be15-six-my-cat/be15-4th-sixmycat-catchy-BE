package com.sixmycat.catchy.feature.jjure.command.domain.repository;

import com.sixmycat.catchy.feature.jjure.command.domain.aggregate.JjureComment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JjureCommentRepository extends JpaRepository<JjureComment, Long> {
    boolean existsById(Long id);
    void deleteAllByParentCommentId(Long parentCommentId);
}