package com.sixmycat.catchy.feature.feed.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedCommentCreateRequest;
import com.sixmycat.catchy.feature.feed.command.domain.aggregate.FeedComment;
import com.sixmycat.catchy.feature.feed.command.domain.repository.FeedCommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedCommentCommandServiceImpl implements FeedCommentCommandService {

    private final FeedCommentRepository commentRepository;

    @Override
    @Transactional
    public Long createComment(FeedCommentCreateRequest request, Long memberId) {
        if (request.getParentCommentId() != null) {
            boolean exists = commentRepository.existsById(request.getParentCommentId());
            if (!exists) {
                throw new BusinessException(ErrorCode.COMMENT_NOT_FOUND);
            }
        }

        FeedComment comment = FeedComment.create(
                memberId,
                request.getTargetId(),
                request.getTargetType(),
                request.getContent(),
                request.getParentCommentId()
        );

        return commentRepository.save(comment).getCommentId();
    }


}