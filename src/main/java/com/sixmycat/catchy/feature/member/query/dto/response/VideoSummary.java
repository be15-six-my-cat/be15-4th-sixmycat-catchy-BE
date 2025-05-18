package com.sixmycat.catchy.feature.member.query.dto.response;

public record VideoSummary(
        Long videoId,
        String thumbnailUrl
) {}

/* 좋아요 자체의 정보(누가, 언제 눌렀는지 등)를 프론트에 보여줄 필요가 없다면, 좋아요 DTO는 필요 없음*/
