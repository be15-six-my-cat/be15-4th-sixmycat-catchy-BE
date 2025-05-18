package com.sixmycat.catchy.feature.feed.command.application.service;

import com.sixmycat.catchy.feature.feed.command.application.dto.request.FeedCreateRequest;
import com.sixmycat.catchy.feature.feed.command.domain.aggregate.Feed;
import com.sixmycat.catchy.feature.feed.command.domain.repository.FeedRepository;
import com.sixmycat.catchy.feature.feed.command.domain.service.FeedDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedCommandServiceImpl implements FeedCommandService {

    private final FeedDomainService feedDomainService;
    private final FeedRepository feedRepository;

    @Override
    @Transactional
    public Long createFeed(FeedCreateRequest request, Long memberId) {
        // 1. 유효성 검증(비즈니스 로직)
        feedDomainService.validateContentLength(request.getContent());
        feedDomainService.validateImageCount(request.getImageUrls());

        // 2. Feed 객체 생성
        Feed feed = Feed.create(request.getContent(), memberId, request.getMusicUrl(), request.getImageUrls());

        // 3. 저장 후 feedId 반환
        return feedRepository.save(feed).getId();
    }
}