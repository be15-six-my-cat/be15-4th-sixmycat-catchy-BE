package com.sixmycat.catchy.feature.auth.command.application.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SocialLoginResponse {
    private final boolean isNewUser;
    private final String email;
    private final Long userId;
    private final String accessToken;
    private final String refreshToken;

    // 명시적 생성자
    public SocialLoginResponse(boolean isNewUser, String email, Long userId, String accessToken, String refreshToken) {
        this.isNewUser = isNewUser;
        this.email = email;
        this.userId = userId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    // 정적 생성 메서드
    public static SocialLoginResponse loggedIn(Long userId, String accessToken, String refreshToken) {
        return new SocialLoginResponse(false, null, userId, accessToken, refreshToken);
    }

    public static SocialLoginResponse requireAdditionalInfo(String email) {
        return new SocialLoginResponse(true, email, null, null, null);
    }
}
