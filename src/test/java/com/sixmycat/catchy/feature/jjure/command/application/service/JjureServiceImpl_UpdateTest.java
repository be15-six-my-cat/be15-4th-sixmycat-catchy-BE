package com.sixmycat.catchy.feature.jjure.command.application.service;

import com.sixmycat.catchy.exception.BusinessException;
import com.sixmycat.catchy.exception.ErrorCode;
import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureUpdateRequest;
import com.sixmycat.catchy.feature.jjure.command.domain.aggregate.Jjure;
import com.sixmycat.catchy.feature.jjure.command.domain.repository.JjureRepository;
import com.sixmycat.catchy.feature.jjure.command.domain.service.MemberValidationService;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class JjureServiceImpl_UpdateTest {

    @InjectMocks
    private JjureServiceImpl jjureService;

    @Mock
    private JjureRepository jjureRepository;

    @Mock
    private MemberValidationService memberValidationService;

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("성공 - 유효한 요청 시 Jjure 객체의 caption과 fileKey가 업데이트된다")
    void givenValidUpdateRequest_whenUpdateJjure_thenUpdatesFields() {
        // given
        Long memberId = 1L;
        Long jjureId = 10L;

        Jjure jjure = Jjure.builder()
                .id(jjureId)
                .memberId(memberId)
                .caption("이전 설명")
                .fileKey("old-file.mp4")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(jjureRepository.findById(jjureId)).thenReturn(Optional.of(jjure));

        JjureUpdateRequest request = new JjureUpdateRequest("수정된 설명", "updated-file.mp4", jjureId);

        // when
        jjureService.updateJjure(request, memberId);

        // then
        assertEquals("수정된 설명", jjure.getCaption());
        assertEquals("updated-file.mp4", jjure.getFileKey());
    }

    @Test
    @DisplayName("예외 - 존재하지 않는 쭈르 ID로 수정하면 JJURE_NOT_FOUND 예외 발생")
    void givenNonExistingJjureId_whenUpdate_thenThrowsNotFound() {
        // given
        Long memberId = 1L;
        JjureUpdateRequest request = new JjureUpdateRequest("내용", "file.mp4", 999L);

        when(jjureRepository.findById(999L)).thenReturn(Optional.empty());

        // when & then
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            jjureService.updateJjure(request, memberId);
        });

        assertEquals(ErrorCode.JJURE_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    @DisplayName("예외 - 본인이 아닌 다른 회원이 수정하면 NO_PERMISSION_TO_UPDATE_JJURE 예외 발생")
    void givenWrongMemberId_whenUpdate_thenThrowsUnauthorized() {
        // given
        Long ownerId = 1L;
        Long otherMemberId = 99L;
        Long jjureId = 100L;

        Jjure jjure = Jjure.builder()
                .id(jjureId)
                .memberId(ownerId)
                .caption("이전 설명")
                .fileKey("file.mp4")
                .build();

        when(jjureRepository.findById(jjureId)).thenReturn(Optional.of(jjure));

        JjureUpdateRequest request = new JjureUpdateRequest("수정된 설명", "new.mp4", jjureId);

        // when & then
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            jjureService.updateJjure(request, otherMemberId);
        });

        assertEquals(ErrorCode.NO_PERMISSION_TO_UPDATE_JJURE, ex.getErrorCode());
    }


    @Test
    @DisplayName("유효성 실패 - fileKey가 빈 문자열이면 실패")
    void givenEmptyFileKey_whenValidate_thenFails() {
        JjureUpdateRequest request = new JjureUpdateRequest("설명", "", 1L);
        Set<ConstraintViolation<JjureUpdateRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fileKey")));
    }
}
