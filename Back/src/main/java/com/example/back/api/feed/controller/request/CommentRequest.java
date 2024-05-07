package com.example.back.api.feed.controller.request;

public record CommentRequest(
        Long feedInfoId,
        String text
) {
}
