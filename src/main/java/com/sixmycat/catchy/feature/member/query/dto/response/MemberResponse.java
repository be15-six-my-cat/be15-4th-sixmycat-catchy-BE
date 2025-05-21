package com.sixmycat.catchy.feature.member.query.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class MemberResponse {
    private Long memberId;
    private String nickname;
    private String statusMessage;
    private String profileImage;
}
