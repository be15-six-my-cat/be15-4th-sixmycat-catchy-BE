package com.sixmycat.catchy.exception.errorCode;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum UserErrorCode {

    ALREADY_REGISTERED_EMAIL("10001", "이미 가입한 아이디입니다.", HttpStatus.CONFLICT);

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
