package com.sixmycat.catchy.feature.auth.command.domain.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.auth.command.domain.aggregate.TempMember;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class OAuth2UserInfoExtractorImpl implements OAuth2UserInfoExtractor {

    @Override
    public String extractEmail(DefaultOAuth2User user, String platform) {
        return switch (platform.toLowerCase()) {
            case "naver" -> ((Map<String, Object>) user.getAttributes().get("response")).get("email").toString();
            case "kakao" -> ((Map<String, Object>) user.getAttributes().get("kakao_account")).get("email").toString();
            default -> throw new BusinessException(ErrorCode.SOCIAL_PLATFORM_NOT_SUPPORTED);
        };
    }

    @Override
    public TempMember extractTempMember(DefaultOAuth2User user, String platform) {
        return TempMember.builder()
                .email(extractEmail(user, platform))
                .social(platform.toUpperCase())
                .name(extractName(user, platform))
                .contactNumber(extractPhone(user, platform))
                .profileImage(extractProfileImage(user, platform))
                .build();
    }

    private String extractName(DefaultOAuth2User user, String platform) {
        return platform.equals("kakao")
                ? ((Map<String, Object>) ((Map<String, Object>) user.getAttributes().get("kakao_account")).get("profile")).get("nickname").toString()
                : ((Map<String, Object>) user.getAttributes().get("response")).get("name").toString();
    }

    private String extractPhone(DefaultOAuth2User user, String platform) {
        return platform.equals("kakao")
                ? ((Map<String, Object>) user.getAttributes().get("kakao_account")).get("phone_number").toString()
                : ((Map<String, Object>) user.getAttributes().get("response")).get("mobile").toString();
    }

    private String extractProfileImage(DefaultOAuth2User user, String platform) {
        return platform.equals("kakao")
                ? ((Map<String, Object>) ((Map<String, Object>) user.getAttributes().get("kakao_account")).get("profile")).get("profile_image_url").toString()
                : ((Map<String, Object>) user.getAttributes().get("response")).get("profile_image").toString();
    }
}
