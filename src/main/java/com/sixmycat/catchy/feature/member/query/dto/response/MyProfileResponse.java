package com.sixmycat.catchy.feature.member.query.dto.response;

import java.util.List;

public record MyProfileResponse(
        String nickname,
        String statusMessage,
        String profileImg,
        Badges badges,
        int followerCount,
        int followingCount,
        int postCount,

        // 고양이 목록
        List<CatResponse> cats,

        // 아래 4개는 탭 콘텐츠
        List<FeedSummary> myFeeds,
        List<VideoSummary> myVideos,
        List<FeedSummary> likedFeeds,
        List<VideoSummary> likedVideos
) {}
