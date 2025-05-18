package com.sixmycat.catchy.feature.auth.command.application.controller;

import com.sixmycat.catchy.feature.auth.command.application.service.AuthCommandService;
import com.sixmycat.catchy.feature.auth.command.domain.aggregate.TempMember;
import com.sixmycat.catchy.feature.auth.command.application.dto.request.ExtraSignupRequest;
import com.sixmycat.catchy.feature.auth.command.application.dto.response.SocialLoginResponse;
import com.sixmycat.catchy.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class AuthController {

    private final AuthCommandService authCommandService;

    @PostMapping("/signup/extra")
    public ResponseEntity<ApiResponse<SocialLoginResponse>> completeSignup(@RequestBody ExtraSignupRequest request) {
        SocialLoginResponse result = authCommandService.registerNewMember(request);
        return ResponseEntity.ok(ApiResponse.success(result));
    }

    @GetMapping("/temp-info")
    public ResponseEntity<ApiResponse<TempMember>> getTempInfo(@RequestParam String email, @RequestParam String social) {
        TempMember temp = authCommandService.getTempMember(email, social.toUpperCase());
        return ResponseEntity.ok(ApiResponse.success(temp));
    }
}
