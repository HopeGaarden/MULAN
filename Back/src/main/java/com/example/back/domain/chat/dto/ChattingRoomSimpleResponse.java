package com.example.back.domain.chat.dto;

import com.example.back.domain.chat.ChattingRoom;

import java.time.LocalDateTime;

public record ChattingRoomSimpleResponse(
        Long chattingRoomId,
        Long diseaseId,
        String diseaseName
) {
    public static ChattingRoomSimpleResponse of(ChattingRoom chattingRoom, String diseaseName) {
        return new ChattingRoomSimpleResponse(
                chattingRoom.getId(),
                chattingRoom.getDiseaseId(),
                diseaseName
        );
    }
}
