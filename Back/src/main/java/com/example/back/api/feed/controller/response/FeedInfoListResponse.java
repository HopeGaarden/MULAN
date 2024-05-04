package com.example.back.api.feed.controller.response;

import com.example.back.domain.feed.feedinfo.FeedCategory;
import com.example.back.domain.feed.feedinfo.FeedInfo;
import com.example.back.domain.feed.feedinfo.FeedStatus;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public record FeedInfoListResponse(
        Long id,
        String nickName,
        FeedCategory category,
        String content,
        String images,
        FeedStatus status,
        Integer commentCount,
        Integer likeCount,
        Boolean like,
        Boolean bookmark

) {
    public static FeedInfoListResponse ResponseGetFeedInfoList(FeedInfo feedInfo,
                                                               Integer commentCount,
                                                               Integer likeCount,
                                                               Boolean like,
                                                               Boolean bookmark) {
        return FeedInfoListResponse.builder()
                .nickName(feedInfo.getMember().getNickname())
                .category(feedInfo.getCategory())
                .content(feedInfo.getContent())
                .images(feedInfo.getImages())
                .status(feedInfo.getStatus())
                .commentCount(commentCount)
                .likeCount(likeCount)
                .like(like)
                .bookmark(bookmark)
                .build();
    }
}
