package com.sixmycat.catchy.feature.member.query.dto.response;

public record Badges(
        boolean isRankOne,       // 랭킹 1위 여부 (왕관 뱃지)
        boolean isInfluencer,    // 인플루언서 여부
        boolean isBirthday       // 생일 뱃지 여부
) {}
