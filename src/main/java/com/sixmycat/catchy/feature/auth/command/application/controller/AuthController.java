package com.sixmycat.catchy.feature.auth.command.application.controller;

import com.sixmycat.catchy.common.dto.ApiResponse;
import com.sixmycat.catchy.common.utils.CookieUtils;
import com.sixmycat.catchy.feature.auth.command.application.dto.request.ExtraSignupRequest;
import com.sixmycat.catchy.feature.auth.command.application.dto.response.SocialLoginResponse;
import com.sixmycat.catchy.feature.auth.command.application.service.AuthCommandService;
import com.sixmycat.catchy.feature.auth.command.domain.aggregate.TempMember;
import com.sixmycat.catchy.security.jwt.JwtTokenProvider;
import com.sixmycat.catchy.common.dto.TokenResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.sixmycat.catchy.common.utils.CookieUtils.createRefreshTokenCookie;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/members")
public class AuthController {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthCommandService authCommandService;

    @PostMapping("/signup/extra")
    public ResponseEntity<ApiResponse<SocialLoginResponse>> completeSignup(@RequestBody ExtraSignupRequest request) {
        SocialLoginResponse result = authCommandService.registerNewMember(request);

        // refreshToken만 쿠키로 내려주고
        ResponseCookie refreshTokenCookie = CookieUtils.createRefreshTokenCookie(result.getRefreshToken());

        // accessToken은 바디로 전달
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(ApiResponse.success(
                        SocialLoginResponse.builder()
                                .id(result.getId())
                                .accessToken(result.getAccessToken())
                                .refreshToken(null) // 바디에는 포함시키지 않음
                                .build()
                ));
    }

    @GetMapping("/temp-info")
    public ResponseEntity<ApiResponse<TempMember>> getTempInfo(@RequestParam String email, @RequestParam String social) {
        TempMember temp = authCommandService.getTempMember(email, social.toUpperCase());
        return ResponseEntity.ok(ApiResponse.success(temp));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken != null) {
            authCommandService.logout(refreshToken);
        }

        ResponseCookie deleteCookie = CookieUtils.createDeleteRefreshTokenCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(ApiResponse.success(null));
    }

    @GetMapping("/token")
    public ResponseEntity<ApiResponse<SocialLoginResponse>> reissueAccessToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken == null || !jwtTokenProvider.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // userId 추출
        Long userId = Long.parseLong(jwtTokenProvider.getUserIdFromJwt(refreshToken));

        // accessToken 재발급
        String newAccessToken = jwtTokenProvider.createToken(userId);

        SocialLoginResponse response = SocialLoginResponse.loggedInWithoutRefresh(userId, newAccessToken);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    /* 테스트 로그인 */
    @Operation(summary = "테스트용 로그인", description = "테스트용 로그인 후 JWT를 발급합니다.")
    @PostMapping("/login/test")
    public ResponseEntity<ApiResponse<TokenResponse>> login(){
        TokenResponse token = authCommandService.testLogin();
        return buildTokenResponse(token);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponse<Void>> withdraw(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        if (refreshToken != null) {
            authCommandService.delete(refreshToken);
        }

        ResponseCookie deleteCookie = CookieUtils.createDeleteRefreshTokenCookie();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, deleteCookie.toString())
                .body(ApiResponse.success(null));
    }

    /* accessToken 과 refreshToken을 body와 쿠키에 담아 반환 */
    private ResponseEntity<ApiResponse<TokenResponse>> buildTokenResponse(TokenResponse tokenResponse) {
        ResponseCookie cookie = createRefreshTokenCookie(tokenResponse.getRefreshToken());  // refreshToken 쿠키 생성
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(ApiResponse.success(tokenResponse));
    }
}
