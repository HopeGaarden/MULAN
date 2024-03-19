package com.example.back.domain.auth.member.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    ADMIN("관리자"),
    UNAUTH("미인증"),
    MEMBER("회원"),
    WITHDRAW("탈퇴");

    private final String role;
}
