package com.sixmycat.catchy.feature.jjure.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureUpdateRequest;
import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureUploadRequest;
import com.sixmycat.catchy.feature.jjure.command.domain.aggregate.Jjure;
import com.sixmycat.catchy.feature.jjure.command.domain.repository.JjureRepository;
import com.sixmycat.catchy.feature.jjure.command.domain.service.MemberValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class JjureServiceImpl implements JjureService {

    private final JjureRepository jjureRepository;
    private final MemberValidationService memberValidationService;

    @Override
    public void uploadJjure(JjureUploadRequest request, Long memberId) {
        // member 탈퇴여부 확인
        memberValidationService.validateUploadable(memberId);

        LocalDateTime now = LocalDateTime.now();

        Jjure jjure = Jjure.builder()
                .memberId(memberId)
                .caption(request.getCaption())
                .fileKey(request.getFileKey())
                .musicUrl(null) // 음악 설정 연결 전까지 일단 Null로 처리
                .createdAt(now)
                .updatedAt(now)
                .deletedAt(null)
                .build();

        try {
            jjureRepository.save(jjure);
        } catch (DataAccessException e) {
            throw new BusinessException(ErrorCode.JJURE_UPLOAD_FAILED);
        }
    }

    @Override
    public void updateJjure(JjureUpdateRequest request, Long memberId) {
        memberValidationService.validateUploadable(memberId);

        Jjure jjure = jjureRepository.findById(request.getJjureId())
                .orElseThrow(() -> new BusinessException(ErrorCode.JJURE_NOT_FOUND));

        if (!jjure.getMemberId().equals(memberId)) {
            throw new BusinessException(ErrorCode.NO_PERMISSION_TO_UPDATE_JJURE);
        }

        jjure.update(request.getCaption(), request.getFileKey());
    }
}

