package com.example.back.domain.chat.repository;

import com.example.back.domain.chat.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {

    ChattingRoom save(ChattingRoom chattingRoom);

    Optional<ChattingRoom> findByDiseaseId(Long diseaseId);

    boolean existsByDiseaseId(Long diseaseId);
}
