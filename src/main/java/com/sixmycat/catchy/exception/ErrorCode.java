package com.sixmycat.catchy.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ErrorCode {

    VALIDATION_ERROR("10001", "입력 값 검증 오류입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("10002", "내부 서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED_USER("10003", "인증되지 않은 사용자입니다.",HttpStatus.UNAUTHORIZED),
    EXPIRED_JWT("10004", "JWT 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_JWT("10005", "잘못된 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_JWT("10006", "지원하지 않는 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED),
    EMPTY_JWT("10007", "JWT 클레임이 비어있습니다.", HttpStatus.UNAUTHORIZED),

    // 피드
    FEED_NOT_FOUND("04000", "피드를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    FEED_CONTENT_TOO_LONG("04001", "피드 내용은 500자 이하여야 합니다.", HttpStatus.BAD_REQUEST),
    FEED_IMAGE_REQUIRED("04002", "피드에는 최소 1개의 이미지가 필요합니다.", HttpStatus.BAD_REQUEST),
    FEED_IMAGE_TOO_MANY("04003", "피드에는 최대 5개의 이미지만 등록할 수 있습니다.", HttpStatus.BAD_REQUEST),

    //댓글
    COMMENT_NOT_FOUND("11000", "해당 상위댓글을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),

    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
