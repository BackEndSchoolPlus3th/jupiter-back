package com.jupiter.wyl.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ExceptionCode {
    USER_EMAIL_NOT_FOUND("USER_4041", "존재하지 않는 이메일입니다."),
    USER_INVALID_PASSWORD("USER_4001", "비밀번호가 올바르지 않습니다."),
    EMAIL_ALREADY_REGISTERED("USER_4092", "이미 등록된 이메일입니다."),
    NICKNAME_ALREADY_TAKEN("USER_4093", "이미 사용 중인 닉네임입니다."),

    AUTH_TOKEN_EXPIRED("AUTH_4011", "토큰이 만료되었습니다."),
    AUTH_INVALID_TOKEN("AUTH_4012", "유효하지 않은 토큰입니다.");

    private final String code;
    private final String message;
}
