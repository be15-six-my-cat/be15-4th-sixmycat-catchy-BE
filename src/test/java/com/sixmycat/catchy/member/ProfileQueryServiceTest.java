package com.sixmycat.catchy.member;

import com.sixmycat.catchy.feature.member.command.domain.aggregate.Cat;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.User;
import com.sixmycat.catchy.feature.member.command.domain.repository.CatRepository;
import com.sixmycat.catchy.feature.member.command.domain.repository.FollowRepository;
import com.sixmycat.catchy.feature.member.command.domain.repository.UserRepository;
import com.sixmycat.catchy.feature.member.query.dto.response.Badges;
import com.sixmycat.catchy.feature.member.query.dto.response.MyProfileResponse;
import com.sixmycat.catchy.feature.member.query.mapper.ProfileMapper;
import com.sixmycat.catchy.feature.member.query.service.ProfileQueryService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class ProfileQueryServiceTest {

    @InjectMocks
    private ProfileQueryService profileQueryService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FollowRepository followRepository;

    @Mock
    private ProfileMapper profileMapper;

    @Test
    @DisplayName("프로필 정보 조회")
    void getMyProfile() {
        // given
        Long userId = 1L;

        // 생성자 사용
        User user = new User("길동이", "안녕하세요!", "default1.png");

        // 고양이 정보 생성 및 연관관계 설정
        Cat cat = new Cat("나비", "F", "코리안숏헤어", LocalDate.now(), 4, user);
        user.addCat(cat); // 양방향 연관관계 설정 메서드 사용

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(followRepository.countByFollower_Id(userId)).willReturn(60000);
        given(followRepository.countByFollowing_Id(userId)).willReturn(123);

        Badges badges = new Badges(true, true, true);

        MyProfileResponse expectedResponse = new MyProfileResponse(
                "길동이",
                "안녕하세요!",
                "default1.png",
                badges,
                60000,
                123,
                0,
                List.of(),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );

        given(profileMapper.toMyProfileResponse(
                eq(user),
                eq(60000),
                eq(123),
                eq(0),
                any(Badges.class),
                anyList(),
                anyList(),
                anyList(),
                anyList()
        )).willReturn(expectedResponse);

        // when
        MyProfileResponse actual = profileQueryService.getMyProfile(userId);

        // then
        assertThat(actual).isEqualTo(expectedResponse);
    }
}

