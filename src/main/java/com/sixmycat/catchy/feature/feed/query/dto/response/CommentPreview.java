package com.sixmycat.catchy.feature.feed.query.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CommentPreview {
    private String userNickname;
    private String content;
}