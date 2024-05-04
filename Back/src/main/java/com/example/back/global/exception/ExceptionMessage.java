package com.example.back.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {
    // MemberException
    MEMBER_ROLE_NOT_FOUND("회원의 권한 정보를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND("회원 정보를 찾을 수 없습니다."),
    MEMBER_PASSWORD_DO_NOT_MATCH("입력된 비밀번호가 서로 일치하지 않습니다."),
    MEMBER_EMAIL_ALREADY_EXIST("해당 이메일은 이미 존재하는 이메일입니다."),

    // JwtTokenException
    JWT_SUBJECT_IS_NULL("JWT 토큰의 식별자가 NULL입니다."),
    JWT_TOKEN_EXPIRED("JWT 토큰이 만료되었습니다."),
    JWT_UNSUPPORTED("지원하지 않는 JWT 토큰입니다."),
    JWT_MALFORMED("JWT 토큰의 형태가 올바르지 않습니다."),
    JWT_SIGNATURE("JWT 토큰의 SIGNATURE가 올바르지 않습니다."),
    JWT_TOKEN_INVALID( "더 이상 사용할 수 없는 JWT 토큰입니다."),
    JWT_ILLEGAL_ARGUMENT("JWT 토큰의 구성 요소가 올바르지 않습니다."),
    JWT_NOT_FOUND("요청의 헤더에서 JWT 토큰을 읽어올 수 없습니다."),
    REFRESH_TOKEN_IS_NULL("Refresh 토큰의 값이 null입니다."),
    REFRESH_TOKEN_INVALID("더 이상 사용할 수 없는 Refresh 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND("Refresh 토큰을 찾을 수 없습니다."),
    REFRESH_TOKEN_REVOKED("철회 상태의 Refresh 토큰입니다."),

    // MailException
    MAIL_SEND_MAX_RETRY_EXCEEDED("메일 재전송 최대 횟수를 초과하였습니다."),
    MAIL_SEND_FAIL("알 수 없는 에러로 메일 전송에 실패하였습니다."),
    MAIL_SEND_INTERRUPTED("매일 재전송 대기 중 인터럽트가 발생했습니다."),

    // FeedInfoException
    FEEDINFO_NOT_FOUND("피드 정보를 찾을 수 없습니다. "),

    // TagInfoException
    TAGINFO_NOT_FOUND("태그 정보를 찾을 수 없습니다. "),

    // CommentException
    COMMENT_NOT_FOUND("댓글 정보를 찾을 수 없습니다. "),

    // LikeException
    Like_NOT_FOUND("좋아요 정보를 찾을 수 없습니다. "),


    ;
    private final String text;
}
