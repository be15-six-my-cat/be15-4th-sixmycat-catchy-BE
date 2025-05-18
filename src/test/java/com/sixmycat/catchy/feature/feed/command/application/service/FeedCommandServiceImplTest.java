package com.sixmycat.catchy.feature.feed.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedCreateRequest;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedUpdateRequest;
import com.sixmycat.catchy.feature.feed.command.domain.aggregate.Feed;
import com.sixmycat.catchy.feature.feed.command.domain.repository.FeedRepository;
import com.sixmycat.catchy.feature.feed.command.domain.service.FeedDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedCommandServiceImplTest {

    private FeedDomainService feedDomainService;
    private FeedRepository feedRepository;
    private FeedCommandServiceImpl feedCommandService;

    @BeforeEach
    void setUp() {
        feedDomainService = mock(FeedDomainService.class);
        feedRepository = mock(FeedRepository.class);
        feedCommandService = new FeedCommandServiceImpl(feedDomainService, feedRepository);
    }

    @Test
    void shouldCreateFeedSuccessfully() {
        // given
        FeedCreateRequest request = new FeedCreateRequest("content", List.of("url1", "url2"), "musicUrl");
        Long memberId = 1L;

        Feed mockFeed = Feed.create("content", memberId, "musicUrl", List.of("url1", "url2"));
        when(feedRepository.save(any(Feed.class))).thenReturn(mockFeed);

        // when
        Long result = feedCommandService.createFeed(request, memberId);

        // then
        assertThat(result).isEqualTo(mockFeed.getId());
        verify(feedDomainService).validateContentLength("content");
        verify(feedDomainService).validateImageCount(List.of("url1", "url2"));
        verify(feedRepository).save(any(Feed.class));
    }

    @Test
    void shouldThrowExceptionWhenImageListIsEmpty() {
        // given
        FeedCreateRequest request = new FeedCreateRequest("content", List.of(), "musicUrl");
        Long memberId = 1L;

        doThrow(new BusinessException(ErrorCode.FEED_IMAGE_REQUIRED)).when(feedDomainService).validateImageCount(any());

        // when & then
        assertThatThrownBy(() -> feedCommandService.createFeed(request, memberId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.FEED_IMAGE_REQUIRED.getMessage());

        verify(feedDomainService).validateContentLength("content");
        verify(feedDomainService).validateImageCount(List.of());
        verify(feedRepository, never()).save(any());
    }

    @Test
    void shouldUpdateFeedSuccessfully() {
        // given
        Long feedId = 1L;
        Long memberId = 1L;
        Feed feed = Feed.create("oldContent", memberId, "oldMusic", List.of("oldUrl1"));

        FeedUpdateRequest updateRequest = new FeedUpdateRequest("newContent", "newMusic", List.of("newUrl1", "newUrl2"));

        when(feedRepository.findById(feedId)).thenReturn(Optional.of(feed));

        // when
        feedCommandService.updateFeed(feedId, updateRequest, memberId);

        // then
        verify(feedDomainService).validateFeedOwner(feed, memberId);
        verify(feedDomainService).validateContentLength("newContent");
        verify(feedDomainService).validateImageCount(List.of("newUrl1", "newUrl2"));

        assertThat(feed.getContent()).isEqualTo("newContent");
        assertThat(feed.getMusicUrl()).isEqualTo("newMusic");
        assertThat(feed.getFeedImages()).hasSize(2);
        assertThat(feed.getFeedImages().get(0).getImageUrl()).isEqualTo("newUrl1");
    }

    @Test
    void shouldThrowWhenFeedNotFoundOnUpdate() {
        // given
        Long feedId = 100L;
        FeedUpdateRequest request = new FeedUpdateRequest("newContent", "music", List.of("url"));
        when(feedRepository.findById(feedId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> feedCommandService.updateFeed(feedId, request, 1L))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining(ErrorCode.FEED_NOT_FOUND.getMessage());

        verify(feedRepository).findById(feedId);
    }
}