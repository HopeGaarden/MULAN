package com.example.back.domain.group.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GroupMemberStatus {
    MEMBER("그룹 멤버"),
    WITHDRAW("탈퇴한 멤버");

    private final String status;
}