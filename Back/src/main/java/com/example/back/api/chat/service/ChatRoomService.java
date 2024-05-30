package com.example.back.api.chat.service;

import com.example.back.api.chat.dto.ChatMessageRequest;
import com.example.back.domain.auth.disease.DiseaseInfo;
import com.example.back.domain.auth.disease.DiseaseMember;
import com.example.back.domain.auth.disease.repository.DiseaseInfoRepository;
import com.example.back.domain.auth.disease.repository.DiseaseMemberRepository;
import com.example.back.domain.auth.medical.MedicalInfo;
import com.example.back.domain.auth.medical.repository.MedicalInfoRepository;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.chat.Chat;
import com.example.back.domain.chat.ChattingRoom;
import com.example.back.domain.chat.dto.ChatHistoryResponse;
import com.example.back.domain.chat.dto.ChattingRoomSimpleResponse;
import com.example.back.domain.chat.repository.ChatRepository;
import com.example.back.domain.chat.repository.ChattingRoomRepository;
import com.example.back.global.exception.DiseaseMemberException;
import com.example.back.global.exception.ExceptionMessage;
import com.example.back.global.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final ChatRepository chatRepository;
    private final MedicalInfoRepository medicalInfoRepository;

    @Transactional
    public ChattingRoomSimpleResponse findMyChat(Member member) {

        MedicalInfo medicalInfo = medicalInfoRepository.findByMember(member).orElseThrow(() -> {
            log.warn("[BR WARN]: {}", ExceptionMessage.MEMBER_NOT_FOUND.getText());
            throw new MemberException(ExceptionMessage.MEMBER_NOT_FOUND);
        });

        ChattingRoom myChat = chattingRoomRepository.findByDiseaseId(medicalInfo.getDiseaseInfoId());

        return ChattingRoomSimpleResponse.of(myChat, medicalInfo.getDiseaseInfoName());
    }

    @Transactional
    public ChattingRoom createChattingRoom(Member member) {

        MedicalInfo medicalInfo = medicalInfoRepository.findByMember(member).orElseThrow(() -> {
            log.warn("[BR WARN]: {}", ExceptionMessage.MEMBER_NOT_FOUND.getText());
            throw new MemberException(ExceptionMessage.MEMBER_NOT_FOUND);
        });

        ChattingRoom chattingRoom = ChattingRoom.createNewChattingRoom(medicalInfo.getDiseaseInfoId());

        return chattingRoomRepository.save(chattingRoom);
    }

    @Transactional
    public List<ChatHistoryResponse> findChattingHistoryByChatId(Long memberId, Long chattingRoomId, Long chatId, Integer pageSize) {
        return chatRepository.findChattingHistoryByChatId(memberId, chattingRoomId, chatId, pageSize);
    }

    @Transactional
    public Chat chat(final Long chatRoomId, final ChatMessageRequest chattingRequest) {
        return chatRepository.save(Chat.of(chatRoomId, chattingRequest.senderId(), chattingRequest.message()));
    }
}
