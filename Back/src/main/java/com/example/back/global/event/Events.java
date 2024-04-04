package com.example.back.global.event;

import org.springframework.context.ApplicationEventPublisher;

public class Events {

    // 이벤트 발생기
    private static ApplicationEventPublisher publisher;

    public static void setPublisher(final ApplicationEventPublisher publisher) {
        Events.publisher = publisher;
    }

    // 이벤트 발생
    public static void raise(final Object event) {
        if (publisher != null) {
            publisher.publishEvent(event);
        }
    }
}
