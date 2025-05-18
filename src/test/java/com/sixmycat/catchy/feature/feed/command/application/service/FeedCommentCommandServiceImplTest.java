package com.sixmycat.catchy.feature.feed.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedCommentCreateRequest;
import com.sixmycat.catchy.feature.feed.command.domain.aggregate.FeedComment;
import com.sixmycat.catchy.feature.feed.command.domain.repository.FeedCommentRepository;
import com.sixmycat.catchy.common.domain.TargetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedCommentCommandServiceImplTest {

    private FeedCommentRepository commentRepository;
    private FeedCommentCommandServiceImpl commentService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(FeedCommentRepository.class);
        commentService = new FeedCommentCommandServiceImpl(commentRepository);
    }

    @Test
    void shouldCreateCommentSuccessfully() {
        // given
        FeedCommentCreateRequest request = mock(FeedCommentCreateRequest.class);
        Long memberId = 1L;
        Long targetId = 2L;

        when(request.getTargetId()).thenReturn(targetId);
        when(request.getTargetType()).thenReturn(TargetType.FEED);
        when(request.getContent()).thenReturn("test content");
        when(request.getParentCommentId()).thenReturn(null);

        FeedComment mockComment = FeedComment.create(memberId, targetId, TargetType.FEED, "test content", null);
        when(commentRepository.save(any(FeedComment.class))).thenReturn(mockComment);

        // when
        Long result = commentService.createComment(request, memberId);

        // then
        assertThat(result).isEqualTo(mockComment.getId());
        verify(commentRepository).save(any(FeedComment.class));
    }

    @Test
    void shouldThrowWhenParentCommentNotFound() {
        // given
        FeedCommentCreateRequest request = mock(FeedCommentCreateRequest.class);
        when(request.getParentCommentId()).thenReturn(999L);
        when(commentRepository.existsById(999L)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> commentService.createComment(request, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.COMMENT_NOT_FOUND.getMessage());
    }

    @Test
    void shouldDeleteCommentSuccessfully() {
        // given
        Long commentId = 1L;
        Long memberId = 1L;
        FeedComment comment = FeedComment.create(memberId, 2L, TargetType.FEED, "hi", null);

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // when
        commentService.deleteComment(commentId, memberId);

        // then
        verify(commentRepository).delete(comment); // SQLDelete → 논리 삭제됨
    }

    @Test
    void shouldThrowWhenCommentNotFoundOnDelete() {
        // given
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(1L, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.COMMENT_NOT_FOUND.getMessage());
    }

    @Test
    void shouldThrowWhenUnauthorizedUserDeletesComment() {
        // given
        FeedComment comment = FeedComment.create(1L, 2L, TargetType.FEED, "hi", null);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(1L, 999L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.UNAUTHORIZED_USER.getMessage());
    }
}
