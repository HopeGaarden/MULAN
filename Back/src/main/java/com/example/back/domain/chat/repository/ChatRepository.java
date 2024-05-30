package com.example.back.domain.chat.repository;

import com.example.back.domain.chat.Chat;
import com.example.back.domain.chat.dto.ChatHistoryResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    Chat save(Chat chat);

    @Query("SELECT new com.example.back.domain.chat.dto.ChatHistoryResponse(" +
            "c.chatRoomId, c.id, c.senderId, m.nickname, c.message, " +
            "(c.senderId = :memberId), c.createdDateTime) " +
            "FROM Chat c LEFT JOIN Member m ON m.id = c.senderId " +
            "WHERE c.chatRoomId = :chattingRoomId AND " +
            "( :chatId IS NULL OR c.id < :chatId) " +
            "ORDER BY c.createdDateTime DESC")
    List<ChatHistoryResponse> findChattingHistoryByChatId(
            @Param("authId") Long memberId,
            @Param("chattingRoomId") Long chattingRoomId,
            @Param("chatId") Long chatId,
            @Param("pageSize") Integer pageSize);
}
