package com.sixmycat.catchy.feature.feed.query.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sixmycat.catchy.common.dto.PageResponse;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.feed.query.dto.response.*;
import com.sixmycat.catchy.feature.feed.query.mapper.FeedQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedQueryServiceImpl implements FeedQueryService {

    private final FeedQueryMapper feedQueryMapper;

    @Override
    public FeedDetailResponse getFeedDetail(Long feedId, Long userId) {
        // 1. 기본 정보
        FeedBaseInfo baseInfo = feedQueryMapper.findFeedBaseInfo(feedId)
                .orElseThrow(() -> new BusinessException(ErrorCode.FEED_NOT_FOUND));

        // 2. 이미지 리스트
        List<String> imageUrls = feedQueryMapper.findFeedImageUrls(feedId);

        // 3. 최근 댓글 1개
        CommentPreview commentPreview = feedQueryMapper.findLatestCommentPreview(feedId)
                .orElse(null);

        // 4. 좋아요 여부
        boolean isLiked = userId != null && feedQueryMapper.isFeedLikedByUser(feedId, userId);

        // 5. 작성자 본인 여부
        boolean isMine = userId != null && userId.equals(baseInfo.getAuthorId());

        // 6. 응답 조립
        return FeedDetailResponse.builder()
                .id(feedId)
                .author(AuthorInfo.builder()
                        .authorId(baseInfo.getAuthorId())
                        .nickname(baseInfo.getNickname())
                        .profileImageUrl(baseInfo.getProfileImageUrl())
                        .build())
                .imageUrls(imageUrls)
                .content(baseInfo.getContent())
                .musicUrl(baseInfo.getMusicUrl())
                .likeCount(baseInfo.getLikeCount())
                .commentCount(baseInfo.getCommentCount())
                .commentPreview(commentPreview)
                .isLiked(isLiked)
                .isMine(isMine)
                .createdAt(baseInfo.getCreatedAt())
                .build();
    }

    @Override
    public PageResponse<FeedSummaryResponse> getMyFeeds(Long memberId, int page, int size) {
        PageHelper.startPage(page + 1, size); // PageHelper는 1부터 시작
        List<FeedSummaryResponse> list = feedQueryMapper.findMyFeeds(memberId);
        return PageResponse.from(new PageInfo<>(list));
    }
}