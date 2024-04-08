package com.example.back.alarm.domain.auth;

import com.example.back.global.event.Event;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class RegisteredEvent extends Event {
    private final Long memberId;
    private final String email;
    private final String nickname;
}
