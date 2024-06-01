package com.example.back.domain.chat.repository;

import com.example.back.domain.chat.dto.ChatHistoryResponse;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.back.domain.auth.member.QMember.member;
import static com.example.back.domain.chat.QChat.chat;

@RequiredArgsConstructor
public class ChatRepositoryCustomImpl implements ChatRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ChatHistoryResponse> findChattingHistoryByChatId(Long memberId, Long chattingRoomId, Long chatId, Integer pageSize) {

        return queryFactory.select(Projections.constructor(ChatHistoryResponse.class,
                        chat.chatRoomId,
                        chat.id,
                        chat.senderId,
                        member.nickname,
                        chat.message,
                        chat.senderId.eq(memberId),
                        chat.createdDateTime))
                .from(chat)
                .leftJoin(member).on(member.id.eq(chat.senderId))
                .where(chat.chatRoomId.eq(chattingRoomId)
                        .and(chatId != null ? chat.id.lt(chatId) : null))
                .orderBy(chat.createdDateTime.desc())
                .limit(pageSize)
                .fetch();
    }
}
