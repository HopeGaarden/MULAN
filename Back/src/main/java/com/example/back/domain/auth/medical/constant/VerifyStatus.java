package com.example.back.domain.auth.medical.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@RequiredArgsConstructor
public enum VerifyStatus {
    PENDING("인증 요청중"),
    VERIFIED("인증 환료"),
    FAILED("실패");

    private final String status;
}
