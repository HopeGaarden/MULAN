package com.example.back.domain.feed.feedInfo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FeedCategory {
    DIARY("질병 일기"),
    SHARE("정보 공유"),
    FREE("자유");

    private final String category;
}
