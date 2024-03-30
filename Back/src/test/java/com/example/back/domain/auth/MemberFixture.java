package com.example.back.domain.auth;

import com.example.back.domain.auth.member.Member;
import com.example.back.domain.auth.member.constant.MemberRole;

public class MemberFixture {

    public static Member 일반_유저_생성() {
        return Member.builder()
                .nickname("닉네임")
                .password("password")
                .email("email@naver.com")
                .role(MemberRole.MEMBER)
                .build();
    }
}

