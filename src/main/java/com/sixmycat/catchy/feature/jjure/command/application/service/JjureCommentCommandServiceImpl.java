package com.sixmycat.catchy.feature.jjure.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.command.domain.aggregate.FeedComment;
import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureCommentCreateRequest;
import com.sixmycat.catchy.feature.jjure.command.domain.aggregate.JjureComment;
import com.sixmycat.catchy.feature.jjure.command.domain.repository.JjureCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JjureCommentCommandServiceImpl implements JjureCommentCommandService {

    private final JjureCommentRepository commentRepository;

    @Override
    @Transactional
    public Long createComment(JjureCommentCreateRequest request, Long memberId) {
        if (request.getParentCommentId() != null) {
            JjureComment parent = commentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

            if (!parent.getTargetType().equals(request.getTargetType())) {
                throw new BusinessException(ErrorCode.INVALID_PARENT_COMMENT);
            }
        }

        JjureComment comment = JjureComment.create(
                memberId,
                request.getTargetId(),
                request.getTargetType(),
                request.getContent(),
                request.getParentCommentId()
        );

        return commentRepository.save(comment).getCommentId();
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId, Long memberId) {
        JjureComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BusinessException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_USER);
        }

        // 재귀적으로 자식 댓글 포함 삭제
        deleteRecursively(commentId);
    }

    private void deleteRecursively(Long commentId) {
        List<JjureComment> children = commentRepository.findAllByParentCommentId(commentId);
        for (JjureComment child : children) {
            deleteRecursively(child.getCommentId());
        }
        commentRepository.deleteById(commentId);
    }

}