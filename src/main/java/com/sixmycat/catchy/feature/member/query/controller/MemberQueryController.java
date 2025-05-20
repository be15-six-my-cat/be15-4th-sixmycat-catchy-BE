package com.sixmycat.catchy.feature.member.query.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.exception.CustomJwtException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.member.query.dto.response.MemberResponse;
import com.sixmycat.catchy.feature.member.query.service.MemberQueryService;
import com.sixmycat.catchy.security.jwt.JwtTokenProvider;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/members")
public class MemberQueryController {

    private final MemberQueryService memberQueryService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping("/me")
    public ApiResponse<MemberResponse> getMyInfo(@RequestHeader("Authorization") String authHeader) {
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            throw new CustomJwtException(ErrorCode.EMPTY_JWT);
        }

        String token = authHeader.substring(7);

        try {
            jwtTokenProvider.validateToken(token);
        } catch (ExpiredJwtException e) {
            throw new CustomJwtException(ErrorCode.EXPIRED_JWT);
        } catch (UnsupportedJwtException e) {
            throw new CustomJwtException(ErrorCode.UNSUPPORTED_JWT);
        } catch (MalformedJwtException | SignatureException e) {
            throw new CustomJwtException(ErrorCode.INVALID_JWT);
        } catch (Exception e) {
            throw new CustomJwtException(ErrorCode.UNAUTHORIZED_USER);
        }

        Long memberId = Long.parseLong(jwtTokenProvider.getUserIdFromJwt(token));
        MemberResponse member = memberQueryService.findById(memberId);

        return ApiResponse.success(member);
    }
}
