package com.sixmycat.catchy.feature.member.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.member.command.application.dto.request.UpdateCatRequest;
import com.sixmycat.catchy.feature.member.command.application.dto.request.UpdateProfileRequest;
import com.sixmycat.catchy.feature.member.command.application.dto.response.UpdateProfileResponse;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.Cat;
import com.sixmycat.catchy.feature.member.command.domain.aggregate.Member;
import com.sixmycat.catchy.feature.member.command.domain.repository.MemberRepository;
import com.sixmycat.catchy.feature.member.query.dto.response.CatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberCommandServiceImpl implements MemberCommandService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public UpdateProfileResponse updateProfile(Long memberId, UpdateProfileRequest request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateProfile(
                request.getNickname(),
                request.getStatusMessage(),
                request.getProfileImage()
        );

        List<CatResponse> catResponses = null;

        if (request.getCats() != null) {
            catResponses = request.getCats().stream()
                    .map(catReq -> {
                        Cat cat = member.getCats().stream()
                                .filter(c -> c.getId().equals(catReq.getId()))
                                .findFirst()
                                .orElseThrow(() -> new BusinessException(ErrorCode.CAT_NOT_FOUND));

                        cat.updateCatInfo(
                                catReq.getName(),
                                catReq.getGender(),
                                catReq.getBreed(),
                                catReq.getBirthDay(),
                                catReq.getAge()
                        );

                        return new CatResponse(
                                cat.getId(),
                                cat.getName(),
                                cat.getGender(),
                                cat.getBreed(),
                                cat.getBirthDate(),
                                cat.getAge()
                        );
                    })
                    .toList();
        }

        return new UpdateProfileResponse(
                member.getId(),
                member.getNickname(),
                member.getStatusMessage(),
                member.getProfileImage(),
                catResponses
        );
    }
}
