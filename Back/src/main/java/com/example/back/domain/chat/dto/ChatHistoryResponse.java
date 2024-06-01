package com.example.back.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record ChatHistoryResponse(
        Long chatRoomId,
        Long chattingId,
        Long senderId,
        String senderNickname,
        String message,
        Boolean isSendByMe,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime sendTime
) {
}
