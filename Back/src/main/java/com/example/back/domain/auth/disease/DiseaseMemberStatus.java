package com.example.back.domain.auth.disease;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DiseaseMemberStatus {
    MEMBER("그룹 멤버"),
    WITHDRAW("탈퇴한 멤버");

    private final String status;
}