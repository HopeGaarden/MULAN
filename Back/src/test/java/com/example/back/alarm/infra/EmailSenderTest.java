package com.example.back.alarm.infra;


import com.example.back.alarm.domain.MailSource;
import jakarta.mail.Session;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Properties;

import static com.example.back.config.IntegrationHelper.NON_ASCII;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@SuppressWarnings(NON_ASCII)
@ExtendWith(MockitoExtension.class)
class EMailSenderTest {
    @Mock
    private JavaMailSender javaMailSender;

    @InjectMocks
    private EmailSender emailSender;

    @Captor
    private ArgumentCaptor<MimeMessage> mimeMessageCaptor;

    @Test
    void 메일을_성공적으로_전송한다() throws Exception {
        // given
        String receiver = "test@example.com";
        Long id = 1L;
        String nickname = "TestNickname";

        Session session = Session.getInstance(new Properties());
        MimeMessage mimeMessage = new MimeMessage(session);
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // when
        emailSender.pushMail(receiver, id, nickname);

        // then
        verify(javaMailSender).send(mimeMessageCaptor.capture()); // MimeMessage 객체 캡처

        // 캡처된 MimeMessage에서 내용 추출
        MimeMessage capturedMessage = mimeMessageCaptor.getValue();
        String content = (String) capturedMessage.getContent();

        // 메일 수신자 검증
        assertEquals(receiver, ((InternetAddress) capturedMessage.getRecipients(MimeMessage.RecipientType.TO)[0]).getAddress());

        // 메일 제목 검증
        assertEquals(MailSource.TITLE.getMessage(), capturedMessage.getSubject());

        // 메일 내용 검증
        String expectedContent = MailSource.getMailMessage(1L, "TestNickname");
        assertEquals(expectedContent, content);
    }
}