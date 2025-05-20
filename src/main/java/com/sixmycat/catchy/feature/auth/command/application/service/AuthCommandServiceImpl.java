package com.sixmycat.catchy.feature.auth.command.application.service;

import com.sixmycat.catchy.common.dto.TokenResponse;
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
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {

    private final MemberRepository memberRepository;

    @Qualifier("tempMemberRedisTemplate")
    private final RedisTemplate<String, TempMember> redisTemplate;

    @Qualifier("refreshTokenRedisTemplate")
    private final RedisTemplate<String, RefreshToken> refreshTokenRedisTemplate;

    private final JwtTokenProvider jwtTokenProvider;

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
                Duration.ofMillis(jwtTokenProvider.getRefreshTokenExpiration())
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

    /* 테스트 로그인  */
    @Transactional
    public TokenResponse testLogin() {

        // 토큰 발급
        String accessToken = jwtTokenProvider.createToken(1L);
        String refreshToken = jwtTokenProvider.createRefreshToken(1L);

        return TokenResponse
                .builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void logout(String refreshToken) {
        Set<String> keys = refreshTokenRedisTemplate.keys("REFRESH_TOKEN:*");
        if (keys != null) {
            for (String key : keys) {
                RefreshToken storedToken = refreshTokenRedisTemplate.opsForValue().get(key);
                if (storedToken != null && refreshToken.equals(storedToken.getToken())) {
                    refreshTokenRedisTemplate.delete(key);
                    break;
                }
            }
        }
    }

    @Override
    @Transactional
    public void delete(String refreshToken) {
        // Redis에서 memberId 찾기
        Set<String> keys = refreshTokenRedisTemplate.keys("REFRESH_TOKEN:*");
        if (keys == null) return;

        for (String key : keys) {
            RefreshToken storedToken = refreshTokenRedisTemplate.opsForValue().get(key);
            if (storedToken != null && refreshToken.equals(storedToken.getToken())) {
                // key: REFRESH_TOKEN:{memberId}
                Long memberId = Long.parseLong(key.split(":")[1]);

                // DB에서 회원 조회 및 탈퇴 처리
                Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

                member.updateDeletedAt();

                // 저장
                memberRepository.save(member);

                // Redis에서 refreshToken 제거
                refreshTokenRedisTemplate.delete(key);
                break;
            }
        }
    }
}
