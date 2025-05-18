package com.sixmycat.catchy.feature.auth.command.application.service;

import com.sixmycat.catchy.common.utils.NicknameValidator;
import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.auth.command.domain.aggregate.RefreshToken;
import com.sixmycat.catchy.feature.auth.command.domain.aggregate.TempMember;
import com.sixmycat.catchy.feature.auth.command.application.dto.request.ExtraSignupRequest;
import com.sixmycat.catchy.feature.auth.command.application.dto.response.SocialLoginResponse;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.Member;
import com.sixmycat.catchy.feature.member.command.domain.repository.MemberRepository;
import com.sixmycat.catchy.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {

    private final MemberRepository memberRepository;
    private final RedisTemplate<String, TempMember> redisTemplate;

    @Qualifier("refreshTokenRedisTemplate")
    private final RedisTemplate<String, RefreshToken> refreshTokenRedisTemplate;

    private final JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenExpiration;

    @Override
    public SocialLoginResponse registerNewMember(ExtraSignupRequest request) {
        String key = switch (request.getSocial().toUpperCase()) {
            case "NAVER" -> "TEMP_N_MEMBER:" + request.getEmail();
            case "KAKAO" -> "TEMP_K_MEMBER:" + request.getEmail();
            default -> throw new BusinessException(ErrorCode.SOCIAL_PLATFORM_NOT_SUPPORTED);
        };

        TempMember temp = redisTemplate.opsForValue().get(key);
        if (temp == null) {
            throw new BusinessException(ErrorCode.TEMP_MEMBER_NOT_FOUND);
        }

        String nickname = request.getNickname();

        // 닉네임 공백 검사
        if (NicknameValidator.isEmptyOrBlank(nickname)) {
            throw new BusinessException(ErrorCode.EMPTY_OR_BLANK_NICKNAME);
        }

        // 닉네임 길이 검사
        if (!NicknameValidator.isLengthValid(nickname)) {
            throw new BusinessException(ErrorCode.WRONG_NICKNAME_LENGTH);
        }

        // 닉네임 유효성 검사
        if (!NicknameValidator.isPatternValid(nickname)) {
            throw new BusinessException(ErrorCode.INVALID_NICKNAME_FORMAT);
        }

        // 닉네임 중복 검사
        if (memberRepository.existsByNicknameAndDeletedAtIsNull(nickname)) {
            throw new BusinessException(ErrorCode.USING_NICKNAME);
        }

        Member member = Member.builder()
                .email(temp.getEmail())
                .social(temp.getSocial())
                .profileImage(temp.getProfileImage())
                .name(request.getName())
                .contactNumber(request.getContactNumber())
                .nickname(nickname)
                .build();

        memberRepository.save(member);
        redisTemplate.delete(key);

        Long memberId = member.getId();
        String accessToken = jwtTokenProvider.createToken(memberId);
        String refreshToken = jwtTokenProvider.createRefreshToken(memberId);

        refreshTokenRedisTemplate.opsForValue().set(
                "REFRESH_TOKEN:" + memberId,
                new RefreshToken(refreshToken),
                Duration.ofMillis(refreshTokenExpiration)
        );

        return SocialLoginResponse.loggedIn(memberId, accessToken, refreshToken);
    }

    @Override
    public TempMember getTempMember(String email, String social) {
        String key = switch (social.toUpperCase()) {
            case "NAVER" -> "TEMP_N_MEMBER:" + email;
            case "KAKAO" -> "TEMP_K_MEMBER:" + email;
            default -> throw new BusinessException(ErrorCode.SOCIAL_PLATFORM_NOT_SUPPORTED);
        };

        TempMember temp = redisTemplate.opsForValue().get(key);
        if (temp == null) {
            throw new BusinessException(ErrorCode.TEMP_MEMBER_NOT_FOUND);
        }
        return temp;
    }
}
