package com.example.back.domain.auth.member.constant;

import com.example.back.common.exception.ExceptionMessage;
import com.example.back.common.exception.MemberException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum MemberRole {
    ADMIN("관리자"),
    UNAUTH("미인증"),
    MEMBER("회원"),
    WITHDRAW("탈퇴");

    private final String role;

    public static MemberRole from(final String role) {
        return Arrays.stream(values())
                .filter(value -> value.role.equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> {
                    log.error("[BR ERROR] {} : {}", ExceptionMessage.MEMBER_ROLE_NOT_FOUND.getText(), role);
                    throw new MemberException(ExceptionMessage.MEMBER_ROLE_NOT_FOUND);
                });
    }
}
