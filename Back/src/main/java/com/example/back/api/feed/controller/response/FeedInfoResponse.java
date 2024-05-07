package com.example.back.api.feed.controller.response;

import com.example.back.domain.feed.feedinfo.FeedCategory;
import com.example.back.domain.feed.feedinfo.FeedInfo;
import com.example.back.domain.feed.feedinfo.FeedStatus;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Builder
public record FeedInfoResponse(
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
    public static FeedInfoResponse ResponseGetFeedInfo(FeedInfo feedInfo,
                                                       Integer commentCount,
                                                       Integer likeCount,
                                                       Boolean like,
                                                       Boolean bookmark){
        return  new FeedInfoResponse(
                feedInfo.getMember().getNickname(),
                feedInfo.getCategory(),
                feedInfo.getContent(),
                feedInfo.getImages(),
                feedInfo.getStatus(),
                commentCount,
                likeCount,
                like,
                bookmark
        );
    }
}
