package com.example.back.domain.chat.dto;

import java.time.LocalDateTime;

public record ChattingRoomSimpleResponse(
        Long chattingRoomId,
        Long diseaseId,
        String diseaseName,
        LocalDateTime lastChattingTime
) {
}
