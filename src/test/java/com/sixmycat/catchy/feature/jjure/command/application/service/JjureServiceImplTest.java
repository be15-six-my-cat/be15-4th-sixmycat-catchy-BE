package com.sixmycat.catchy.feature.jjure.command.application.service;

import com.sixmycat.catchy.feature.jjure.command.application.dto.request.JjureUploadRequest;
import com.sixmycat.catchy.feature.jjure.command.domain.aggregate.Jjure;
import com.sixmycat.catchy.feature.jjure.command.domain.repository.JjureRepository;
import com.sixmycat.catchy.feature.jjure.command.domain.service.MemberValidationService;
import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JjureServiceImplTest {

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
    @DisplayName("성공 - 올바른 요청 시 Jjure 저장 성공")
    void givenValidRequest_whenUploadJjure_thenSavesSuccessfully() {
        // given
        JjureUploadRequest request = new JjureUploadRequest("쭈르 캡션", "uploads/cat.mp4");

        // when
        jjureService.uploadJjure(request, 1L);

        // then
        verify(memberValidationService).validateUploadable(1L);
        verify(jjureRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("유효성 실패 - fileKey가 null이면 실패")
    void givenNullFileKey_whenValidate_thenFails() {
        JjureUploadRequest request = new JjureUploadRequest("쭈르 설명", null);

        Set<ConstraintViolation<JjureUploadRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fileKey")));
    }

    @Test
    @DisplayName("유효성 실패 - fileKey가 빈 문자열이면 실패")
    void givenEmptyFileKey_whenValidate_thenFails() {
        JjureUploadRequest request = new JjureUploadRequest("쭈르 설명", "");

        Set<ConstraintViolation<JjureUploadRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("fileKey")));
    }

    @Test
    @DisplayName("상태 변화 - 저장된 Jjure의 필드가 정확히 설정되어야 함")
    void givenValidRequest_whenUploadJjure_thenFieldsShouldBeCorrect() {
        // given
        String caption = "쭈르 설명입니다";
        String fileKey = "uploads/video.mp4";
        JjureUploadRequest request = new JjureUploadRequest(caption, fileKey);
        ArgumentCaptor<Jjure> captor = ArgumentCaptor.forClass(Jjure.class);

        // when
        jjureService.uploadJjure(request, 1L);

        // then
        verify(jjureRepository, times(1)).save(captor.capture());
        Jjure saved = captor.getValue();

        assertEquals(caption, saved.getCaption());
        assertEquals(fileKey, saved.getFileKey());
        assertNull(saved.getMusicUrl());
        assertNotNull(saved.getCreatedAt());
        assertNotNull(saved.getUpdatedAt());
        assertNull(saved.getDeletedAt());
    }

    @Test
    @DisplayName("예외 처리 - 저장 중 Repository 예외 발생 시 예외 전달")
    void givenRepositoryFails_whenUploadJjure_thenThrowsException() {
        JjureUploadRequest request = new JjureUploadRequest("쭈르 설명", "uploads/fail.mp4");
        doThrow(new RuntimeException("DB ERROR")).when(jjureRepository).save(any());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            jjureService.uploadJjure(request, 1L);
        });

        assertEquals("DB ERROR", exception.getMessage());
    }

    @Test
    @DisplayName("예외 처리 - 유효하지 않은 요청 시 저장 로직 호출되지 않아야 함")
    void givenInvalidRequest_whenUploadJjure_thenRepositoryNotCalled() {
        JjureUploadRequest request = new JjureUploadRequest(null, null);
        Set<ConstraintViolation<JjureUploadRequest>> violations = validator.validate(request);

        assertFalse(violations.isEmpty());
        verify(jjureRepository, never()).save(any());
    }
}
