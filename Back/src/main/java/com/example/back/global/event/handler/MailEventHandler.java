package com.example.back.global.event.handler;

import com.example.back.alarm.domain.MailSender;
import com.example.back.alarm.domain.auth.RegisteredEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MailEventHandler {

    private final MailSender mailSender;

    @Async
    @EventListener(RegisteredEvent.class)
    public void sendMail(final RegisteredEvent event) {
        log.info("[" + event.getMemberId() + "번 유저 (nickname : " + event.getNickname() + ") 생성 완료 : 회원가입 축하 메일 발송 완료");

        try {
            mailSender.pushMail(event.getEmail(), event.getMemberId(), event.getNickname());
        } catch (final Exception exception) {
            handleErrors(event, exception);
        }
    }

    private void handleErrors(final RegisteredEvent event, final Exception exception) {
        log.error("이메일 전송 실패 member : " + event.getMemberId() + " " + event.getEmail());
        log.error(exception.getMessage());

        // TODO : 에러 핸들링 (메일 재전송)
    }
}


//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class MailEventHandler {
//
//    private final MailSender mailSender;
//    private static final int MAX_RETRY = 3; // 최대 재시도 횟수
//    private static final long RETRY_DELAY = 5000L; // 재시도 사이의 딜레이 시간(밀리초)
//
//    @Async
//    @EventListener(RegisteredEvent.class)
//    public void sendMail(RegisteredEvent event) {
//        log.info("[BR INFO] [{}번 {}님 회원가입 축하 메일 발송을 시도합니다.", event.getMemberId(), event.getNickname());
//
//        int attempt = 0;
//        boolean isSent = false;
//        while (!isSent) {
//            try {
//                // 메일 전송 시도
//                mailSender.pushMail(event.getEmail(), event.getMemberId(), event.getNickname());
//
//                log.info("[BR INFO] [{}번 {}님 회원가입 축하 메일 발송을 완료하였습니다.", event.getMemberId(), event.getNickname());
//                isSent = true; // 메일 전송 성공
//            } catch (MessagingException | UnsupportedEncodingException exception) {
//                attempt++;
//                log.error("[BR ERROR] 이메일 전송 실패, 재시도 횟수: {} / {}", attempt, MAX_RETRY);
//
//                if (attempt >= MAX_RETRY) {
//                    log.error("[BR ERROR] 최대 재시도 횟수 도달, 메일 발송 실패: {} {}", event.getMemberId(), event.getEmail());
//                    throw new MailException(ExceptionMessage.MAIL_SEND_MAX_RETRY_EXCEEDED);
//                }
//
//                try {
//                    Thread.sleep(RETRY_DELAY); // 다음 시도 전 딜레이
//
//                } catch (InterruptedException ie) {
//                    Thread.currentThread().interrupt(); // 인터럽트 상태 복원
//
//                    log.error("[BR ERROR] 메일 재전송 대기 중 인터럽트 발생");
//                    throw new MailException(ExceptionMessage.MAIL_SEND_INTERRUPTED);
//                }
//            } catch (Exception e) {
//                log.error("[BR ERROR] 알 수 없는 오류가 발생했습니다.", e);
//                throw new MailException(ExceptionMessage.MAIL_SEND_FAIL);
//            }
//        }
//    }
//
//}