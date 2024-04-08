package com.example.back.global.event.handler;

import com.example.back.alarm.domain.MailSender;
import com.example.back.alarm.domain.auth.RegisteredEvent;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.io.UnsupportedEncodingException;

import static com.example.back.config.IntegrationHelper.NON_ASCII;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@SuppressWarnings(NON_ASCII)
@ExtendWith(MockitoExtension.class)
class MailEventHandlerMockTest {

    @InjectMocks
    private MailEventHandler mailEventHandler;

    @Mock
    private MailSender mailSender;

    @Test
    void 메일_발송_성공_테스트() throws MessagingException, UnsupportedEncodingException {
        // given
        RegisteredEvent event = new RegisteredEvent(1L, "email@email.com", "nickname");
        doNothing().when(mailSender).pushMail(any(String.class), any(Long.class), any(String.class));

        // when
        mailEventHandler.sendMail(event);

        // then
        verify(mailSender).pushMail(event.getEmail(), event.getMemberId(), event.getNickname());
    }

    @Test
    void 메일_발송_실패_테스트() throws MessagingException, UnsupportedEncodingException {
        // given
        RegisteredEvent event = new RegisteredEvent(1L, "email@email.com", "nickname");
        doThrow(new RuntimeException("메일 서버 오류")).when(mailSender).pushMail(anyString(), anyLong(), anyString());

        // when
        mailEventHandler.sendMail(event);

        // then
        verify(mailSender).pushMail(event.getEmail(), event.getMemberId(), event.getNickname());
    }


}