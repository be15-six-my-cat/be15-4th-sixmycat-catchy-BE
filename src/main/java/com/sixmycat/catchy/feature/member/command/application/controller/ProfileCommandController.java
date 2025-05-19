package com.sixmycat.catchy.feature.member.command.application.controller;

import com.sixmycat.catchy.feature.member.command.application.dto.request.UpdateProfileRequest;
import com.sixmycat.catchy.feature.member.command.application.dto.response.UpdateProfileResponse;
import com.sixmycat.catchy.feature.member.command.application.service.MemberCommandService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
public class ProfileCommandController {

    private final MemberCommandService memberCommandService;

    @PatchMapping("/me")
    public ResponseEntity<UpdateProfileResponse> updateProfile(
            @AuthenticationPrincipal Object principal,
            @RequestBody UpdateProfileRequest request
    ) {
        System.out.println("Principal: " + principal);
        Long memberId = 1L; // 테스트용
        UpdateProfileResponse response = memberCommandService.updateProfile(memberId, request);
        return ResponseEntity.ok(response);
    }
}
