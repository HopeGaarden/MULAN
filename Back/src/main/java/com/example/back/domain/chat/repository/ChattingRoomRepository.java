package com.example.back.domain.chat.repository;

import com.example.back.domain.chat.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

    ChattingRoom save(ChattingRoom chattingRoom);

    ChattingRoom findByDiseaseId(Long diseaseId);
}
