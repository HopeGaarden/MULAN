package com.example.back.domain.chat.repository;

import com.example.back.domain.chat.ChattingRoom;
import com.example.back.domain.chat.dto.ChattingRoomSimpleResponse;

import java.util.List;

public interface ChattingRoomRepository {

    ChattingRoom save(ChattingRoom chattingRoom);

    List<ChattingRoomSimpleResponse> findMyChattingRooms(Long authId);
}
