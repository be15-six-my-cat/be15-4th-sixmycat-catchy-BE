package com.sixmycat.catchy.feature.member.command.application.service;

import com.sixmycat.catchy.feature.member.command.application.dto.request.UpdateCatRequest;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.Cat;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.Member;
import com.sixmycat.catchy.feature.member.command.domain.repository.MemberRepository;
import com.sixmycat.catchy.feature.member.command.application.dto.request.UpdateProfileRequest;
import com.sixmycat.catchy.feature.member.command.application.dto.response.UpdateProfileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberCommandService {

    private final MemberRepository memberRepository;

    @Transactional
    public UpdateProfileResponse updateProfile(Long memberId, UpdateProfileRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        member.updateProfile(
                request.getNickname(),
                request.getStatusMessage(),
                request.getProfileImage()
        );

        if (request.getCats() != null) {
            for (UpdateCatRequest catReq : request.getCats()) {
                Cat cat = member.getCats().stream()
                        .filter(c -> c.getId().equals(catReq.getId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("고양이를 찾을 수 없습니다."));
                cat.updateCatInfo(
                        catReq.getName(),
                        catReq.getGender(),
                        catReq.getBreed(),
                        catReq.getBirthDay(),
                        catReq.getAge()
                );
            }
        }

        return new UpdateProfileResponse(
                member.getId(),
                member.getNickname(),
                member.getStatusMessage(),
                member.getProfileImage(),
                request.getCats()
        );
    }
}
