package com.sixmycat.catchy.feature.feed.command.domain.service;

import java.util.List;

public interface FeedDomainService {

    void validateContentLength(String content);

    void validateImageCount(List<String> imageUrls);
}