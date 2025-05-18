package com.sixmycat.catchy.feature.feed.query.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.query.dto.response.CommentPreview;
import com.sixmycat.catchy.feature.feed.query.dto.response.FeedBaseInfo;
import com.sixmycat.catchy.feature.feed.query.dto.response.FeedDetailResponse;
import com.sixmycat.catchy.feature.feed.query.mapper.FeedQueryMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class FeedQueryServiceImplTest {

    @Mock
    private FeedQueryMapper feedQueryMapper;

    @InjectMocks
    private FeedQueryServiceImpl feedQueryService;

    public FeedQueryServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getFeedDetail_returnsResponseSuccessfully() {
        // given
        Long feedId = 1L;
        Long userId = 10L;

        FeedBaseInfo baseInfo = new FeedBaseInfo(
                10L,
                "nickname",
                "https://profile.img",
                "some content",
                "https://music.url",
                5,
                2,
                LocalDateTime.now()
        );

        List<String> imageUrls = List.of("img1.jpg", "img2.jpg");
        CommentPreview commentPreview = new CommentPreview("commenter", "nice!");

        when(feedQueryMapper.findFeedBaseInfo(feedId)).thenReturn(Optional.of(baseInfo));
        when(feedQueryMapper.findFeedImageUrls(feedId)).thenReturn(imageUrls);
        when(feedQueryMapper.findLatestCommentPreview(feedId)).thenReturn(Optional.of(commentPreview));
        when(feedQueryMapper.isFeedLikedByUser(feedId, userId)).thenReturn(true);

        // when
        FeedDetailResponse result = feedQueryService.getFeedDetail(feedId, userId);

        // then
        assertThat(result.getId()).isEqualTo(feedId);
        assertThat(result.getAuthor().getAuthorId()).isEqualTo(userId);
        assertThat(result.getImageUrls()).containsExactlyElementsOf(imageUrls);
        assertThat(result.getContent()).isEqualTo("some content");
        assertThat(result.getMusicUrl()).isEqualTo("https://music.url");
        assertThat(result.getCommentPreview().getUserNickname()).isEqualTo("commenter");
        assertThat(result.isLiked()).isTrue();
        assertThat(result.isMine()).isTrue();
    }

    @Test
    void getFeedDetail_throwsException_whenFeedNotFound() {
        // given
        Long feedId = 100L;

        when(feedQueryMapper.findFeedBaseInfo(feedId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> feedQueryService.getFeedDetail(feedId, 10L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.FEED_NOT_FOUND.getMessage());
    }
}