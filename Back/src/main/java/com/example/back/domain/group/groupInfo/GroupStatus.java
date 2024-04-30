package com.example.back.domain.group.groupInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GroupStatus {
    ACTIVE("활성화"),
    INACTIVE("비활성화");

    private final String status;
}