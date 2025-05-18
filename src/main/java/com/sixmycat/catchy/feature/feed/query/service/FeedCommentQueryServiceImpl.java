package com.sixmycat.catchy.feature.feed.query.service;

import com.sixmycat.catchy.feature.feed.query.dto.response.FeedCommentResponse;
import com.sixmycat.catchy.feature.feed.query.mapper.FeedQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedCommentQueryServiceImpl implements FeedCommentQueryService {

    private final FeedQueryMapper feedQueryMapper;

    @Override
    public List<FeedCommentResponse> getComments(Long feedId) {
        return feedQueryMapper.findCommentsByFeedId(feedId);
    }
}

