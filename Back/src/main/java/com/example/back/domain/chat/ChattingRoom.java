package com.example.back.domain.chat;

import com.example.back.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class ChattingRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long diseaseId;

    public static ChattingRoom createNewChattingRoom(final Long diseaseId) {
        return ChattingRoom.builder()
                .diseaseId(diseaseId)
                .build();
    }

}
