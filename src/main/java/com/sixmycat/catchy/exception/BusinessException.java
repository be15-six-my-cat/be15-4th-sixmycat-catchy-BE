package com.sixmycat.catchy.exception;

import com.sixmycat.catchy.exception.errorCode.UserErrorCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private final UserErrorCode errorCode;

    public BusinessException(UserErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
