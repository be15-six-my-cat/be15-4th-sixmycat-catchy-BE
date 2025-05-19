package com.sixmycat.catchy.exception;

import org.springframework.http.HttpStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ErrorCode {

    VALIDATION_ERROR("01001", "입력 값 검증 오류입니다.", HttpStatus.BAD_REQUEST),
    INTERNAL_SERVER_ERROR("01002", "내부 서버 오류입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED_USER("01003", "인증되지 않은 사용자입니다.",HttpStatus.UNAUTHORIZED),
    EXPIRED_JWT("01004", "JWT 토큰이 만료되었습니다.", HttpStatus.UNAUTHORIZED),
    INVALID_JWT("01005", "잘못된 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_JWT("01006", "지원하지 않는 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED),
    EMPTY_JWT("01007", "JWT 클레임이 비어있습니다.", HttpStatus.UNAUTHORIZED),
    SOCIAL_PLATFORM_NOT_SUPPORTED("01008", "지원하지 않는 소셜 플랫폼입니다.", HttpStatus.BAD_REQUEST),
    TEMP_MEMBER_NOT_FOUND("01009", "임시 회원 정보를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    USING_NICKNAME("01010", "이미 사용중인 닉네임입니다.", HttpStatus.BAD_REQUEST),
    INVALID_NICKNAME_FORMAT("01011", "닉네임 형식이 올바르지 않습니다.", HttpStatus.BAD_REQUEST),
    EMPTY_OR_BLANK_NICKNAME("01012", "닉네임은 공백이거나 비워둘 수 없습니다.", HttpStatus.BAD_REQUEST),
    WRONG_NICKNAME_LENGTH("01013", "닉네임 길이는 3~30자입니다.", HttpStatus.BAD_REQUEST),

    // 멤버
    MEMBER_NOT_FOUND("01000", "회원을 찾을 수 업습니다", HttpStatus.NOT_FOUND),
    MEMBER_ALREADY_DELETED("01001", "탈퇴한 회원입니다", HttpStatus.CONFLICT),

    // 피드
    FEED_NOT_FOUND("04000", "피드를 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    FEED_CONTENT_TOO_LONG("04001", "피드 내용은 500자 이하여야 합니다.", HttpStatus.BAD_REQUEST),
    FEED_IMAGE_REQUIRED("04002", "피드에는 최소 1개의 이미지가 필요합니다.", HttpStatus.BAD_REQUEST),
    FEED_IMAGE_TOO_MANY("04003", "피드에는 최대 5개의 이미지만 등록할 수 있습니다.", HttpStatus.BAD_REQUEST),

    // 쮸르
    JJURE_NOT_FOUND("05000","쭈르를 찾을 수 없습니다", HttpStatus.NOT_FOUND),
    JJURE_UPLOAD_FAILED("05001","쭈르를 등록에 실패했습니다", HttpStatus.BAD_REQUEST),
    NO_PERMISSION_TO_UPDATE_JJURE("05002", "수정 권한이 없습니다",HttpStatus.FORBIDDEN),

    //댓글
    COMMENT_NOT_FOUND("11000", "해당 상위댓글을 찾을 수 없습니다.", HttpStatus.BAD_REQUEST),
    INVALID_PARENT_COMMENT("11001", "부모 댓글의 targetType이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String code;
    private final String message;
    private final HttpStatus httpStatus;
}
