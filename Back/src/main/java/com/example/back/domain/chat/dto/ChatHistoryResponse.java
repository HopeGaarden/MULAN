package com.example.back.domain.chat.dto;

import java.time.LocalDateTime;

public record ChatHistoryResponse(
        Long chatRoomId,
        Long chattingId,
        Long senderId,
        String senderNickname,
        String message,
        Boolean isSendByMe,
        LocalDateTime sendTime
) {
}
