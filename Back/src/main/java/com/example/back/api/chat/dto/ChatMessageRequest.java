package com.example.back.api.chat.dto;

public record ChatMessageRequest(
        Long senderId,
        String message
) {
}
