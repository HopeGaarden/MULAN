package com.example.back.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
    // MemberException
    MEMBER_ROLE_NOT_FOUND("회원의 권한 정보를 찾을 수 없습니다."),

    // TokenException
    JWT_SUBJECT_IS_NULL("JWT 토큰의 식별자가 NULL입니다."),
    JWT_TOKEN_EXPIRED("JWT 토큰이 만료되었습니다."),
    JWT_UNSUPPORTED("지원하지 않는 JWT 토큰입니다."),
    JWT_MALFORMED("JWT 토큰의 형태가 올바르지 않습니다."),
    JWT_SIGNATURE("JWT 토큰의 SIGNATURE가 올바르지 않습니다."),
    JWT_TOKEN_INVALID( "더 이상 사용할 수 없는 JWT 토큰입니다."),
    JWT_ILLEGAL_ARGUMENT("JWT 토큰의 구성 요소가 올바르지 않습니다."),



    ;
    private final String text;
}
