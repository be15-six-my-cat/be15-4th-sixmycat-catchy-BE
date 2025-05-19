package com.sixmycat.catchy.member;

import com.sixmycat.catchy.feature.member.command.application.dto.request.UpdateCatRequest;
import com.sixmycat.catchy.feature.member.command.application.dto.request.UpdateProfileRequest;
import com.sixmycat.catchy.feature.member.command.application.dto.response.UpdateProfileResponse;
import com.sixmycat.catchy.feature.member.command.application.service.MemberCommandService;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.Cat;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.Member;
import com.sixmycat.catchy.feature.member.command.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

class MemberCommandServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private MemberCommandService memberCommandService;

    private Member member;
    private Cat cat;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cat = new Cat("코코", "F", "코리안숏헤어", LocalDate.of(2020, 5, 1), 4, null);
        cat.setMember(member);
        // 직접 ID 설정
        ReflectionTestUtils.setField(cat, "id", 1L);

        member = Member.builder()
                .id(1L)
                .nickname("기존닉네임")
                .statusMessage("기존상태")
                .profileImage("old.png")
                .cats(List.of(cat))
                .build();

        cat.setMember(member);
    }

    @Test
    @DisplayName("프로필, 고양이 정보 수정")
    void updateProfile_success() {
        // given
        UpdateCatRequest catRequest = new UpdateCatRequest(
                1L,
                "모카",
                "F",
                "러시안블루",
                LocalDate.of(2019, 4, 20),
                5
        );

        UpdateProfileRequest request = new UpdateProfileRequest(
                "새로운닉",
                "새로운상태",
                "new.png",
                List.of(catRequest)
        );

        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        // when
        UpdateProfileResponse response = memberCommandService.updateProfile(1L, request);

        // then
        assertThat(response.getMemberId()).isEqualTo(1L);
        assertThat(response.getNickname()).isEqualTo("새로운닉");
        assertThat(response.getStatusMessage()).isEqualTo("새로운상태");
        assertThat(response.getProfileImage()).isEqualTo("new.png");
        assertThat(response.getCats().get(0).getName()).isEqualTo("모카");
    }

    @Test
    @DisplayName("해당 ID의 사용자가 없으면 예외를 던진다")
    void updateProfile_fail_userNotFound() {
        // given
        UpdateProfileRequest request = new UpdateProfileRequest(
                "닉", "상태", "이미지", Collections.emptyList()
        );

        given(memberRepository.findById(999L)).willReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> memberCommandService.updateProfile(999L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("고양이 ID가 없으면 예외를 던진다")
    void updateProfile_fail_catNotFound() {
        // given
        UpdateCatRequest catRequest = new UpdateCatRequest(
                99L, "모카", "female", "러시안블루", LocalDate.of(2019, 4, 20), 5
        );

        UpdateProfileRequest request = new UpdateProfileRequest(
                "닉", "상태", "이미지", List.of(catRequest)
        );

        given(memberRepository.findById(1L)).willReturn(Optional.of(member));

        // when & then
        assertThatThrownBy(() -> memberCommandService.updateProfile(1L, request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("고양이를 찾을 수 없습니다.");
    }
}
