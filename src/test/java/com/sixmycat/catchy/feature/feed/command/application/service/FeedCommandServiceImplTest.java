package com.sixmycat.catchy.feature.feed.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedCreateRequest;
import com.sixmycat.catchy.feature.feed.command.domain.aggregate.Feed;
import com.sixmycat.catchy.feature.feed.command.domain.repository.FeedRepository;
import com.sixmycat.catchy.feature.feed.command.domain.service.FeedDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

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

        doThrow(new BusinessException(null, "Image list is empty")).when(feedDomainService).validateImageCount(any());

        // when & then
        assertThatThrownBy(() -> feedCommandService.createFeed(request, memberId))
                .isInstanceOf(BusinessException.class)
                .hasMessageContaining("Image list is empty");

        verify(feedDomainService).validateContentLength("content");
        verify(feedDomainService).validateImageCount(List.of());
        verify(feedRepository, never()).save(any());
    }
}