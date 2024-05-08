package com.example.back.api.feed.controller.response;

import com.example.back.domain.feed.like.Like;
import lombok.Builder;

@Builder
public record LikeMemberResponse(
        String nickname,
        String profileImage
) {
    public static LikeMemberResponse getLikesResponse(Like like){
        return LikeMemberResponse.builder()
                .nickname(like.getMember().getNickname())
                .profileImage(like.getMember().getProfileImageUrl())
                .build();
    }
}
