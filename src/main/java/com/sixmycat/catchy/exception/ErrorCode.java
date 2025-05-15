package com.sixmycat.catchy.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ErrorCode {

    VALIDATION_ERROR("10001", "입력 값 검증 오류입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("10002", "내부 서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
