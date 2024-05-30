package com.example.back.domain.chat;

import com.example.back.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@EqualsAndHashCode(of = "id", callSuper = false)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
public class Chat extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long chatRoomId;

    @Column(nullable = false)
    private Long senderId;

    @Lob
    @Column(nullable = false)
    private String message;

    public static Chat of(final Long chatId, final Long senderId, final String message) {
        return Chat.builder()
                .chatRoomId(chatId)
                .senderId(senderId)
                .message(message)
                .build();
    }
}
