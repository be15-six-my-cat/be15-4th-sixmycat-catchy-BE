package com.sixmycat.catchy.feature.jjure.query.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.common.dto.AuthorInfo;
import com.sixmycat.catchy.common.dto.CommentPreview;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureBaseInfo;
import com.sixmycat.catchy.feature.jjure.query.dto.response.JjureDetailResponse;
import com.sixmycat.catchy.feature.jjure.query.mapper.JjureQueryMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JjureQueryServiceImpl implements JjureQueryService {

    private final JjureQueryMapper jjureQueryMapper;

    @Override
    public JjureDetailResponse getJjureDetail(Long jjureId, Long userId) {
        // 1. 기본 정보
        JjureBaseInfo baseInfo = jjureQueryMapper.findJjureBaseInfo(jjureId)
                .orElseThrow(() -> new BusinessException(ErrorCode.JJURE_NOT_FOUND));

        // 2. 최근 댓글 1개
        CommentPreview commentPreview = jjureQueryMapper.findLatestCommentPreview(jjureId)
                .orElse(null);

        // 3. 좋아요 여부
        boolean isLiked = userId != null && jjureQueryMapper.isJjureLikedByUser(jjureId, userId);

        // 4. 작성자 본인 여부
        boolean isMine = userId != null && userId.equals(baseInfo.getAuthorId());

        // 5. 응답 조립
        return JjureDetailResponse.builder()
                .id(jjureId)
                .author(AuthorInfo.builder()
                        .authorId(baseInfo.getAuthorId())
                        .nickname(baseInfo.getNickname())
                        .profileImageUrl(baseInfo.getProfileImageUrl())
                        .build())
                .fileKey(baseInfo.getFileKey())
                .caption(baseInfo.getCaption())
                .musicUrl(baseInfo.getMusicUrl())
                .likeCount(baseInfo.getLikeCount())
                .commentCount(baseInfo.getCommentCount())
                .commentPreview(commentPreview)
                .isLiked(isLiked)
                .isMine(isMine)
                .createdAt(baseInfo.getCreatedAt())
                .build();
    }
}
