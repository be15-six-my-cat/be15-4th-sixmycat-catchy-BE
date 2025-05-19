package com.sixmycat.catchy.feature.member.query.controller;

import com.sixmycat.catchy.feature.member.query.dto.response.MyProfileResponse;
import com.sixmycat.catchy.feature.member.query.service.ProfileQueryService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
public class ProfileQueryController {

    private final ProfileQueryService profileQueryService;

    @GetMapping("/me")
    public MyProfileResponse getMyProfile() {
//        public MyProfileResponse getMyProfile(@AuthenticationPrincipal CustomUser user) {
//            return profileQueryService.getMyProfile(user.getId());
        Long userId = 1L; // 임시로 1번 유저 고정 (추후 변경 예정)
        return profileQueryService.getMyProfile(userId);
    }
}
