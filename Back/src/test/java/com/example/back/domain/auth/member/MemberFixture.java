package com.example.back.domain.auth.member;

import com.example.back.domain.auth.member.constant.MemberRole;

public class MemberFixture {

    public static Member 일반_유저_생성() {
        return Member.builder()
                .nickname("nickname")
                .password("password")
                .email("email@email.com")
                .role(MemberRole.MEMBER)
                .build();
    }
}
