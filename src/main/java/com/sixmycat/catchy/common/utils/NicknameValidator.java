package com.sixmycat.catchy.common.utils;

import java.util.regex.Pattern;

public class NicknameValidator {

    private static final String NICKNAME_REGEX = "^[a-zA-Z!@#$%^&*()_+=\\[\\]{}|\\\\:;\"'<>,.?/-]+$";
    private static final Pattern PATTERN = Pattern.compile(NICKNAME_REGEX);

    // 공백, 빈 문자열 검사
    public static boolean isEmptyOrBlank(String nickname) {
        return nickname == null || nickname.isBlank() || nickname.contains(" ");
    }

    // 정규식 패턴만 검사
    public static boolean isPatternValid(String nickname) {
        return PATTERN.matcher(nickname).matches();
    }
}
