package com.sixmycat.catchy.feature.jjure.command.application.service;

import com.sixmycat.catchy.common.domain.TargetType;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureCommentCreateRequest;
import com.sixmycat.catchy.feature.jjure.command.domain.aggregate.JjureComment;
import com.sixmycat.catchy.feature.jjure.command.domain.repository.JjureCommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class JjureCommentCommandServiceImplTest {

    private JjureCommentRepository commentRepository;
    private JjureCommentCommandServiceImpl commentService;

    @BeforeEach
    void setUp() {
        commentRepository = mock(JjureCommentRepository.class);
        commentService = new JjureCommentCommandServiceImpl(commentRepository);
    }

    @Test
    void shouldCreateCommentSuccessfully() {
        // given
        JjureCommentCreateRequest request = mock(JjureCommentCreateRequest.class);
        Long memberId = 1L;
        Long targetId = 2L;

        when(request.getTargetId()).thenReturn(targetId);
        when(request.getTargetType()).thenReturn(TargetType.FEED);
        when(request.getContent()).thenReturn("test content");
        when(request.getParentCommentId()).thenReturn(null);

        JjureComment mockComment = JjureComment.create(memberId, targetId, TargetType.FEED, "test content", null);
        when(commentRepository.save(any(JjureComment.class))).thenReturn(mockComment);

        // when
        Long result = commentService.createComment(request, memberId);

        // then
        assertThat(result).isEqualTo(mockComment.getCommentId());
        verify(commentRepository).save(any(JjureComment.class));
    }

    @Test
    void shouldThrowWhenParentCommentNotFound() {
        // given
        JjureCommentCreateRequest request = mock(JjureCommentCreateRequest.class);
        when(request.getParentCommentId()).thenReturn(999L);
        when(commentRepository.existsById(999L)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> commentService.createComment(request, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.COMMENT_NOT_FOUND.getMessage());
    }

}
