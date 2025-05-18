// OAuth2AuthenticationServiceImpl.java
package com.sixmycat.catchy.feature.auth.command.application.service;

import com.sixmycat.catchy.feature.auth.command.domain.aggregate.TempMember;
import com.sixmycat.catchy.feature.auth.command.domain.service.JwtTokenDomainService;
import com.sixmycat.catchy.feature.auth.command.domain.service.OAuth2UserInfoExtractor;
import com.sixmycat.catchy.feature.auth.command.domain.service.TempMemberRedisService;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.Member;
import com.sixmycat.catchy.feature.member.command.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OAuth2AuthenticationServiceImpl implements OAuth2AuthenticationService {

    private final MemberRepository memberRepository;
    private final JwtTokenDomainService jwtTokenDomainService;
    private final TempMemberRedisService tempMemberRedisService;
    private final OAuth2UserInfoExtractor userInfoExtractor;

    @Value("${app.front.signup-extra-url}")
    private String signupExtraPageUrl;

    @Value("${app.front.login-success-url}")
    private String loginSuccessPageUrl;

    @Override
    public String handleOAuth2Login(DefaultOAuth2User oAuth2User, String registrationId) {
        String email = userInfoExtractor.extractEmail(oAuth2User, registrationId);

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            return jwtTokenDomainService.issueAndRedirect(loginSuccessPageUrl, optionalMember.get());
        }

        TempMember tempMember = userInfoExtractor.extractTempMember(oAuth2User, registrationId);
        tempMemberRedisService.save(tempMember);

        return UriComponentsBuilder.fromUriString(signupExtraPageUrl)
                .queryParam("email", tempMember.getEmail())
                .queryParam("social", tempMember.getSocial())
                .build().toUriString();
    }
}
