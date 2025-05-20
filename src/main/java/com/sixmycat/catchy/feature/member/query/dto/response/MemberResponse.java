package com.sixmycat.catchy.feature.member.query.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private Long id;
    private String email;
    private String nickname;
    private String name;
    private String contactNumber;
    private String profileImage;
    private String social;
}
