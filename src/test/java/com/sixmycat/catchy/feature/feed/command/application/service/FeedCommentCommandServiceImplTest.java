// --- FeedCommentCommandServiceImplTest.java ---
package com.sixmycat.catchy.feature.feed.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedCommentCreateRequest;
import com.sixmycat.catchy.feature.feed.command.domain.aggregate.FeedComment;
import com.sixmycat.catchy.feature.feed.command.domain.repository.FeedCommentRepository;
import com.sixmycat.catchy.common.domain.TargetType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


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
        FeedCommentCreateRequest request = new FeedCommentCreateRequest();
        request = new FeedCommentCreateRequest();
        Long memberId = 1L;
        Long targetId = 2L;
        request = new FeedCommentCreateRequest();
        request.getClass(); // to prevent unused
        request = mock(FeedCommentCreateRequest.class);
        when(request.getTargetId()).thenReturn(targetId);
        when(request.getTargetType()).thenReturn(TargetType.FEED);
        when(request.getContent()).thenReturn("test content");
        when(request.getParentCommentId()).thenReturn(null);

        FeedComment mockComment = FeedComment.create(memberId, targetId, TargetType.FEED, "test content", null);
        when(commentRepository.save(any(FeedComment.class))).thenReturn(mockComment);

        // when
        Long result = commentService.createComment(request, memberId);

        // then
        assertThat(result).isEqualTo(mockComment.getCommentId());
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

}
