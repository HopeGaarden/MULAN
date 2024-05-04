package com.example.back.domain.feed.feedinfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedStatus {
    PUBLIC("공개글"),
    PRIVATE("비공개글");

    private final String status;
}
