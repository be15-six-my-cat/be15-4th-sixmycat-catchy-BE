package com.sixmycat.catchy.feature.feed.query.service;

import com.sixmycat.catchy.feature.feed.query.dto.response.FeedCommentResponse;
import java.util.List;

public interface FeedCommentQueryService {
    List<FeedCommentResponse> getComments(Long feedId);
}
