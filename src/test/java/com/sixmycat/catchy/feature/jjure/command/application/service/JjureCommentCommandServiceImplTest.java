package com.sixmycat.catchy.feature.jjure.command.application.service;

import com.sixmycat.catchy.common.domain.TargetType;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.command.domain.aggregate.FeedComment;
import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureCommentCreateRequest;
import com.sixmycat.catchy.feature.jjure.command.domain.aggregate.JjureComment;
import com.sixmycat.catchy.feature.jjure.command.domain.repository.JjureCommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
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
    @DisplayName("부모 댓글이 없는 상태에서 댓글을 성공적으로 생성한다")
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
    @DisplayName("부모 댓글이 존재하지 않으면 예외가 발생한다")
    void shouldThrowWhenParentCommentNotFound() {
        // given
        JjureCommentCreateRequest request = mock(JjureCommentCreateRequest.class);
        when(request.getParentCommentId()).thenReturn(999L);
        when(commentRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.createComment(request, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.COMMENT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("부모 댓글이 존재하고 타겟 타입이 같을 때 답글 생성 성공")
    void shouldCreateReplyCommentWhenParentExistsAndValid() {
        // given
        Long parentId = 10L;
        Long memberId = 1L;
        Long targetId = 2L;

        JjureCommentCreateRequest request = mock(JjureCommentCreateRequest.class);
        when(request.getTargetId()).thenReturn(targetId);
        when(request.getTargetType()).thenReturn(TargetType.JJURE);
        when(request.getContent()).thenReturn("reply");
        when(request.getParentCommentId()).thenReturn(parentId);

        JjureComment parent = JjureComment.create(memberId, targetId, TargetType.JJURE, "parent", null);
        when(commentRepository.findById(parentId)).thenReturn(Optional.of(parent));

        JjureComment reply = JjureComment.create(memberId, targetId, TargetType.JJURE, "reply", parentId);
        when(commentRepository.save(any(JjureComment.class))).thenReturn(reply);

        // when
        Long result = commentService.createComment(request, memberId);

        // then
        assertThat(result).isEqualTo(reply.getCommentId());
        verify(commentRepository).save(any(JjureComment.class));
    }

    @Test
    @DisplayName("댓글 삭제 시 자식 댓글까지 함께 삭제된다")
    void shouldDeleteCommentRecursively() {
        // given
        Long parentId = 1L;
        Long memberId = 1L;

        JjureComment parent = JjureComment.create(memberId, 2L, TargetType.JJURE, "parent", null);
        JjureComment child1 = JjureComment.create(memberId, 2L, TargetType.JJURE, "child1", parentId);
        JjureComment child2 = JjureComment.create(memberId, 2L, TargetType.JJURE, "child2", parentId);

        when(commentRepository.findById(parentId)).thenReturn(Optional.of(parent));
        when(commentRepository.findAllByParentCommentId(parentId)).thenReturn(List.of(child1, child2));
        when(commentRepository.findAllByParentCommentId(child1.getCommentId())).thenReturn(List.of());
        when(commentRepository.findAllByParentCommentId(child2.getCommentId())).thenReturn(List.of());

        // when
        commentService.deleteComment(parentId, memberId);

        // then
        verify(commentRepository).deleteById(child1.getCommentId());
        verify(commentRepository).deleteById(child2.getCommentId());
        verify(commentRepository).deleteById(parentId);
    }

    @Test
    @DisplayName("삭제 시 댓글이 존재하지 않으면 예외 발생")
    void shouldThrowWhenCommentNotFoundOnDelete() {
        // given
        when(commentRepository.findById(1L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(1L, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.COMMENT_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("댓글 작성자가 아니면 삭제 시 예외 발생")
    void shouldThrowWhenUnauthorizedUserDeletesComment() {
        // given
        JjureComment comment = JjureComment.create(1L, 2L, TargetType.FEED, "hi", null);
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // when & then
        assertThatThrownBy(() -> commentService.deleteComment(1L, 999L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.UNAUTHORIZED_USER.getMessage());
    }
}
