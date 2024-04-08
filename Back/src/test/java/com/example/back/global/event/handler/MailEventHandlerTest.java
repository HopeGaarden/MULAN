package com.example.back.global.event.handler;

import com.example.back.alarm.domain.auth.RegisteredEvent;
import com.example.back.config.IntegrationHelper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;

import static com.example.back.config.IntegrationHelper.NON_ASCII;
import static org.mockito.Mockito.verify;

@SuppressWarnings(NON_ASCII)
class MailEventHandlerTest extends IntegrationHelper {

    @Autowired
    private ApplicationContext applicationContext;

    @MockBean
    private MailEventHandler mailEventHandler;

    @Test
    void 회원가입_이벤트가_발행되면_구독자가_동작한다() {
        // given
        RegisteredEvent event = new RegisteredEvent(1L, "email@email.com", "nickname");

        // when
        applicationContext.publishEvent(event);

        // then
        verify(mailEventHandler).sendMail(event);
    }

}