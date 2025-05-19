package com.sixmycat.catchy.feature.member.command.application.dto.response;

import com.sixmycat.catchy.feature.member.command.application.dto.request.UpdateCatRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UpdateProfileResponse {
    private Long memberId;
    private String nickname;
    private String statusMessage;
    private String profileImage;

    private List<UpdateCatRequest> cats;
}
