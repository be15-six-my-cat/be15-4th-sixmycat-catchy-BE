package com.sixmycat.catchy.feature.member.command.application.service;

import com.sixmycat.catchy.feature.member.command.application.dto.request.UpdateProfileRequest;
import com.sixmycat.catchy.feature.member.command.application.dto.response.UpdateProfileResponse;

public interface MemberCommandService {
    UpdateProfileResponse updateProfile(Long memberId, UpdateProfileRequest request);
}