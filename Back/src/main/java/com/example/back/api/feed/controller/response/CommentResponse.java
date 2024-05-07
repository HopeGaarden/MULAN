package com.example.back.api.feed.controller.response;

import com.example.back.domain.feed.comment.Comment;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponse(
        Long id,
        String nickname,
        String profileImage,
        Long memberId,
        String content,
        LocalDateTime lastModifiedDate
) {
    public static CommentResponse getCommentResponse(Comment comment){
        return CommentResponse.builder()
                .id(comment.getId())
                .nickname(comment.getMember().getNickname())
                .profileImage(comment.getMember().getProfileImageUrl())
                .memberId(comment.getMember().getId())
                .lastModifiedDate(comment.getModifiedDateTime())
                .build();
    }
}
