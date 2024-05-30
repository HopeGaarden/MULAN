package com.example.back.domain.chat.repository;

import com.example.back.domain.chat.Chat;
import com.example.back.domain.chat.dto.ChatHistoryResponse;

import java.util.List;

public interface ChatRepository {

    Chat save(Chat chat);

    List<ChatHistoryResponse> findChattingHistoryByChatId(Long memberId, Long chattingRoomId, Long chatId, Integer pageSize);
}
