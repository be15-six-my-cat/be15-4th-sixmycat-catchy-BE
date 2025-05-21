package com.sixmycat.catchy.feature.member.command.application.controller;

import com.sixmycat.catchy.feature.member.command.application.dto.request.UpdateProfileRequest;
import com.sixmycat.catchy.feature.member.command.application.dto.response.UpdateProfileResponse;
import com.sixmycat.catchy.feature.member.command.application.service.MemberCommandService;
import com.sixmycat.catchy.feature.member.query.dto.response.MyProfileResponse;
import com.sixmycat.catchy.feature.member.query.service.ProfileQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileCommandController {

    private final MemberCommandService memberCommandService;
    private final ProfileQueryService profileQueryService;

    @PatchMapping("/me")
    public ResponseEntity<UpdateProfileResponse> updateProfile(
//            @AuthenticationPrincipal Long memberId,
            @RequestHeader(value = "X-Debug-Member-Id", required = false) Long debugMemberId, //추후 변경
            @RequestBody UpdateProfileRequest request
    ) {
        Long memberId = debugMemberId != null ? debugMemberId : 1L; //추후 변경

        UpdateProfileResponse response = memberCommandService.updateProfile(memberId, request);
        return ResponseEntity.ok(response);
    }
}
