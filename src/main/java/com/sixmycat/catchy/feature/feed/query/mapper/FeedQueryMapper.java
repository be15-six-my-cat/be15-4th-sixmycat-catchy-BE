package com.sixmycat.catchy.feature.feed.query.mapper;

import com.sixmycat.catchy.feature.feed.query.dto.response.FeedBaseInfo;
import com.sixmycat.catchy.feature.feed.query.dto.response.CommentPreview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FeedQueryMapper {

    Optional<FeedBaseInfo> findFeedBaseInfo(@Param("feedId") Long feedId);

    List<String> findFeedImageUrls(@Param("feedId") Long feedId);

    Optional<CommentPreview> findLatestCommentPreview(@Param("feedId") Long feedId);

    boolean isFeedLikedByUser(@Param("feedId") Long feedId, @Param("userId") Long userId);
}