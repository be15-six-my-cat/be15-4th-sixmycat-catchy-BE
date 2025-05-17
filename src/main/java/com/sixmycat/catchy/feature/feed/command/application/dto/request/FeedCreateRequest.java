package com.sixmycat.catchy.feature.feed.command.application.dto.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class FeedCreateRequest {
    private String content;
    private String musicUrl;
    private List<String> imageUrls; // CloudFront URL 목록
}