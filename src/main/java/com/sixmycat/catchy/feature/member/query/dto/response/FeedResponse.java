package com.sixmycat.catchy.feature.member.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class FeedResponse {
    private int feedCount;
    private List<FeedSummary> myFeeds;
    private List<VideoSummary> myVideos;
    private List<FeedSummary> likedFeeds;
    private List<VideoSummary> likedVideos;
}
