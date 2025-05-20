package com.sixmycat.catchy.feature.auth.command.application.service;

import com.sixmycat.catchy.common.dto.TokenResponse;
import com.sixmycat.catchy.common.utils.NicknameValidator;
import com.sixmycat.catchy.common.s3.S3Uploader;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final S3Uploader s3Uploader;

    @Value("${jwt.refresh-expiration}")
    private Long refreshTokenExpiration;

    @Override
    public SocialLoginResponse registerNewMember(ExtraSignupRequest request, MultipartFile profileImage) {
        // Redis 키를 이메일 기반으로 두 가지 방식 모두 확인
        String email = request.getEmail();
        String naverKey = "TEMP_N_MEMBER:" + email;
        String kakaoKey = "TEMP_K_MEMBER:" + email;

        TempMember temp = null;
        String redisKey = null;

        if (redisTemplate.hasKey(naverKey)) {
            temp = redisTemplate.opsForValue().get(naverKey);
            redisKey = naverKey;
        } else if (redisTemplate.hasKey(kakaoKey)) {
            temp = redisTemplate.opsForValue().get(kakaoKey);
            redisKey = kakaoKey;
        }

        if (temp == null) {
            throw new BusinessException(ErrorCode.TEMP_MEMBER_NOT_FOUND);
        }

        // 닉네임 검증
        String nickname = request.getNickname();
        if (NicknameValidator.isEmptyOrBlank(nickname)) {
            throw new BusinessException(ErrorCode.EMPTY_OR_BLANK_NICKNAME);
        }
        if (!NicknameValidator.isLengthValid(nickname)) {
            throw new BusinessException(ErrorCode.WRONG_NICKNAME_LENGTH);
        }
        if (!NicknameValidator.isPatternValid(nickname)) {
            throw new BusinessException(ErrorCode.INVALID_NICKNAME_FORMAT);
        }
        if (memberRepository.existsByNicknameAndDeletedAtIsNull(nickname)) {
            throw new BusinessException(ErrorCode.USING_NICKNAME);
        }

        // S3에 이미지 업로드 (null 허용)
        String profileImageUrl = null;
        if (profileImage != null && !profileImage.isEmpty()) {
            profileImageUrl = s3Uploader.uploadFile(profileImage, "profile");
        }

        // 최종 Member 저장
        Member member = Member.builder()
                .email(temp.getEmail())
                .social(temp.getSocial()) // Redis에 저장된 social 값 사용
                .name(request.getName())
                .contactNumber(request.getContactNumber())
                .nickname(nickname)
                .profileImage(profileImageUrl)
                .build();

        memberRepository.save(member);
        redisTemplate.delete(redisKey); // 사용 완료 후 삭제

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

    @Override
    public TokenResponse testLogin() {
        String accessToken = jwtTokenProvider.createToken(1L);
        String refreshToken = jwtTokenProvider.createRefreshToken(1L);

        return TokenResponse.builder()
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
        Set<String> keys = refreshTokenRedisTemplate.keys("REFRESH_TOKEN:*");
        if (keys == null) return;

        for (String key : keys) {
            RefreshToken storedToken = refreshTokenRedisTemplate.opsForValue().get(key);
            if (storedToken != null && refreshToken.equals(storedToken.getToken())) {
                Long memberId = Long.parseLong(key.split(":")[1]);

                Member member = memberRepository.findById(memberId)
                        .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));
                member.updateDeletedAt();
                memberRepository.save(member);

                refreshTokenRedisTemplate.delete(key);
                break;
            }
        }
    }
}
