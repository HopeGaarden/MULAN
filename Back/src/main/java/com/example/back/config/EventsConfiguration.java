package com.example.back.config;

import com.example.back.global.event.Events;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class EventsConfiguration {

    // 스프링의 핵심 컨테이너로 빈 정의, 이벤트 발행, 리소스 로딩 등 기능 제공
    private final ApplicationContext applicationContext;

    // InitializingBean: 스프링 빈이 모두 초기화된 후에 실행되는 로직 정의
    @Bean
    public InitializingBean eventInitializer() {
        // Events에 ApplicationContext를 이벤트 발행자로 설정
        return () -> Events.setPublisher(applicationContext);
    }
}
