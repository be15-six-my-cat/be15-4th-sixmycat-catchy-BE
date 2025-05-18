package com.sixmycat.catchy.feature.auth.command.application.service;

import com.sixmycat.catchy.feature.auth.command.application.dto.request.ExtraSignupRequest;
import com.sixmycat.catchy.feature.auth.command.application.dto.response.SocialLoginResponse;
import com.sixmycat.catchy.feature.auth.command.domain.aggregate.TempMember;

public interface AuthCommandService {
    SocialLoginResponse registerNewMember(ExtraSignupRequest request);
    TempMember getTempMember(String email, String social);
    void logout(String refreshToken);
}
