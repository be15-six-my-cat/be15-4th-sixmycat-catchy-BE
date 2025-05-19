package com.sixmycat.catchy.feature.member.query.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.feature.member.query.dto.response.MemberResponse;
import com.sixmycat.catchy.feature.member.query.service.MemberQueryService;
import com.sixmycat.catchy.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberQueryController {

    private final MemberQueryService memberQueryService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/me")
    public ApiResponse<MemberResponse> getMyInfo(HttpServletRequest request) {
        String token = extractAccessToken(request);
        jwtTokenProvider.validateToken(token); // 유효성 검사

        Long memberId = Long.parseLong(jwtTokenProvider.getUserIdFromJwt(token));
        MemberResponse member = memberQueryService.findById(memberId);

        return ApiResponse.success(member);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new IllegalArgumentException("Authorization 헤더가 존재하지 않거나 형식이 잘못되었습니다.");
    }
}
