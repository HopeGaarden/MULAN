package com.example.back.domain.chat;

import com.example.back.domain.BaseEntity;
import com.example.back.domain.chat.vo.ChattingStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class ChattingRoom extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long buyerId;

    @Column(nullable = false)
    private Long sellerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChattingStatus chattingStatus;

    public static ChattingRoom createNewChattingRoom(final Long productId, final Long buyerId, final Long sellerId) {
        return ChattingRoom.builder()
                .productId(productId)
                .buyerId(buyerId)
                .sellerId(sellerId)
                .chattingStatus(ChattingStatus.PROCESS)
                .build();
    }

    public void done() {
        this.chattingStatus = ChattingStatus.DONE;
    }
}
