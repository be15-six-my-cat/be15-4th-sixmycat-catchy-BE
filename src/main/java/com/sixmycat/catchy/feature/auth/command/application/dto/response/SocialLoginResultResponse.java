package com.sixmycat.catchy.feature.auth.command.application.dto.response;

public class SocialLoginResultResponse {
    private final SocialLoginResponse response;
    private final String refreshToken;

    public SocialLoginResultResponse(SocialLoginResponse response, String refreshToken) {
        this.response = response;
        this.refreshToken = refreshToken;
    }

    public SocialLoginResponse getResponse() {
        return response;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
