package com.example.back.api.chat.controller;

import com.example.back.api.auth.service.AuthService;
import com.example.back.api.chat.dto.ChatMessageRequest;
import com.example.back.api.chat.dto.ChatMessageResponse;
import com.example.back.api.chat.service.ChatRoomService;
import com.example.back.api.disease.service.DiseaseMemberService;
import com.example.back.common.response.JsonResult;
import com.example.back.domain.auth.disease.DiseaseMember;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.chat.Chat;
import com.example.back.domain.chat.ChattingRoom;
import com.example.back.domain.chat.dto.ChatHistoryResponse;
import com.example.back.domain.chat.dto.ChattingRoomSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChatRoomController {
    private final ChatRoomService chatRoomService;
    private final AuthService authService;
    private final SimpMessagingTemplate messagingTemplate;


    // TODO: 내 채팅방 조회
    @GetMapping("/chats")
    public ResponseEntity<ChattingRoomSimpleResponse> findMyChat(@AuthenticationPrincipal Member member) {
        Member findMember = authService.isMember(member);
        return ResponseEntity.ok(chatRoomService.findMyChat(findMember));
    }

    // TODO: 채팅방 생성
    @PostMapping("/chats")
    public ResponseEntity<Void> createChattingRoom(@AuthenticationPrincipal Member member) {


        ChattingRoom chattingRoom = chatRoomService.createChattingRoom(member);
        return ResponseEntity.created(URI.create(chattingRoom.getDiseaseId() + "/chat/" + chattingRoom.getId())).build();
    }

    // TODO: 채팅방 채팅 내역 반환
    @GetMapping("/{diseaseId}/chats/{chattingRoomId}")
    public ResponseEntity<List<ChatHistoryResponse>> findChattingHistoryByChatId(
            @AuthenticationPrincipal Member member,
            @PathVariable(name = "diseaseId") Long diseaseId,
            @PathVariable(name = "chattingRoomId") Long chattingRoomId,
            @RequestParam(name = "chatId", required = false) final Long chatId,
            @RequestParam(name = "pageSize") final Integer pageSize
    ) {
        Member findMember = authService.isMember(member);
        return ResponseEntity.ok(chatRoomService.findChattingHistoryByChatId(findMember.getId(), chattingRoomId, chatId, pageSize));
    }


    // TODO: 채팅 보내기
    // linking url : ws://localhost:8060/ws-stomp
    // subscribe url : ws://localhost:8060/ws-stomp/sub/chats/1
    // publish url : ws://localhost:8060/ws-stomp/pub/chats/1/messages
    @MessageMapping("/chats/{chatRoomId}/messages")
    public ChatMessageResponse chat(
            @DestinationVariable final Long chatRoomId,
            final ChatMessageRequest chattingRequest
    ) {
        Chat chat = chatRoomService.chat(chatRoomId, chattingRequest);
        messagingTemplate.convertAndSend("/sub/chats/" + chatRoomId, chattingRequest);
        return ChatMessageResponse.from(chat);
    }


}
