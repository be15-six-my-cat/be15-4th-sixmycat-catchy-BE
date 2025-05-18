package com.sixmycat.catchy.feature.feed.query.service;

import com.sixmycat.catchy.feature.feed.query.dto.response.FeedCommentResponse;
import com.sixmycat.catchy.feature.feed.query.mapper.FeedQueryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class FeedCommentQueryServiceImplTest {

    @Mock
    private FeedQueryMapper feedQueryMapper;

    @InjectMocks
    private FeedCommentQueryServiceImpl feedCommentQueryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getComments_returnsCommentListSuccessfully() {
        // given
        Long feedId = 1L;
        List<FeedCommentResponse> mockComments = List.of(
                FeedCommentResponse.builder()
                        .commentId(1L)
                        .memberId(101L)
                        .nickname("고양이짱")
                        .content("첫 댓글")
                        .createdAt(LocalDateTime.now().minusMinutes(10))
                        .parentCommentId(null)
                        .build(),
                FeedCommentResponse.builder()
                        .commentId(2L)
                        .memberId(102L)
                        .nickname("댓글맨")
                        .content("대댓글입니다")
                        .createdAt(LocalDateTime.now().minusMinutes(5))
                        .parentCommentId(1L)
                        .build()
        );

        when(feedQueryMapper.findCommentsByFeedId(feedId)).thenReturn(mockComments);

        // when
        List<FeedCommentResponse> result = feedCommentQueryService.getComments(feedId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getNickname()).isEqualTo("고양이짱");
        assertThat(result.get(1).getParentCommentId()).isEqualTo(1L);
    }

    @Test
    void getComments_returnsEmptyListWhenNoComments() {
        // given
        Long feedId = 99L;
        when(feedQueryMapper.findCommentsByFeedId(feedId)).thenReturn(List.of());

        // when
        List<FeedCommentResponse> result = feedCommentQueryService.getComments(feedId);

        // then
        assertThat(result).isEmpty();
    }
}