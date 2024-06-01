package com.example.back.api.chat.service;

import com.example.back.api.chat.dto.ChatMessageRequest;
import com.example.back.domain.auth.disease.DiseaseInfo;
import com.example.back.domain.auth.disease.repository.DiseaseInfoRepository;
import com.example.back.domain.auth.medical.MedicalInfo;
import com.example.back.domain.auth.medical.repository.MedicalInfoRepository;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.chat.Chat;
import com.example.back.domain.chat.ChattingRoom;
import com.example.back.domain.chat.dto.ChatHistoryResponse;
import com.example.back.domain.chat.dto.ChattingRoomSimpleResponse;
import com.example.back.domain.chat.repository.ChatRepository;
import com.example.back.domain.chat.repository.ChattingRoomRepository;
import com.example.back.global.exception.DiseaseInfoException;
import com.example.back.global.exception.ExceptionMessage;
import com.example.back.global.exception.MemberException;
import com.example.back.global.exception.handler.ChatException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ChatRoomService {

    private final ChattingRoomRepository chattingRoomRepository;
    private final ChatRepository chatRepository;
    private final MedicalInfoRepository medicalInfoRepository;
    private final DiseaseInfoRepository diseaseInfoRepository;

    @Transactional
    public ChattingRoomSimpleResponse findMyChat(Member member) {

        DiseaseInfo disease = getDiseaseInfo(member);

        ChattingRoom myChat = chattingRoomRepository.findByDiseaseId(disease.getId()).orElseThrow(() -> {
            log.warn("[BR WARN]: {}", ExceptionMessage.CHAT_NOT_FOUND.getText());
            throw new ChatException(ExceptionMessage.CHAT_NOT_FOUND);
        });

        return ChattingRoomSimpleResponse.of(myChat, disease.getName());
    }

    @Transactional
    public ChattingRoom createChattingRoom(Member member) {

        DiseaseInfo disease = getDiseaseInfo(member);

        if (chattingRoomRepository.existsByDiseaseId(disease.getId())) {
            log.warn("[BR WARN]: {}", ExceptionMessage.CHAT_ALREADY_EXIST.getText());
            throw new ChatException(ExceptionMessage.CHAT_ALREADY_EXIST);
        }

        ChattingRoom chattingRoom = ChattingRoom.createNewChattingRoom(disease.getId());

        return chattingRoomRepository.save(chattingRoom);
    }

    private DiseaseInfo getDiseaseInfo(Member member) {
        MedicalInfo medicalInfo = medicalInfoRepository.findByMember(member).orElseThrow(() -> {
            log.warn("[BR WARN]: {}", ExceptionMessage.MEMBER_NOT_FOUND.getText());
            throw new MemberException(ExceptionMessage.MEMBER_NOT_FOUND);
        });

        List<DiseaseInfo> diseaseInfos = diseaseInfoRepository.findByNameAndCode(medicalInfo.getDiseaseInfoName(), medicalInfo.getDiseaseInfoId()).orElseThrow(() -> {
            log.error("[BR ERROR] {}", ExceptionMessage.DISEASE_NOT_FOUND.getText());
            return new DiseaseInfoException(ExceptionMessage.DISEASE_NOT_FOUND);
        });

        return diseaseInfos.get(0);
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
