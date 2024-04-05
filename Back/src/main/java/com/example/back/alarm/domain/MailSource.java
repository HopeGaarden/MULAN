package com.example.back.alarm.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MailSource {

    TITLE("BlueRose 회원가입 성공 안내"),
    CONTENT("""
                    <p>BlueRose 회원가입에 성공하셨습니다.<p>
                    <br>
                    <p>해당 이메일은 회원가입 성공 안내 메시지입니다.<p>
                    <br>
            """);

    private final String message;

    public static String getMailMessage(final Long id, final String nickname) {
        String message = "";

        message += "<div style='margin:20px;'>";
        message += "<h1>BlueRose</h1>";
        message += "<p>" + nickname + "님 환영합니다.<p>";
        message += CONTENT.message;
        message += "</div>";

        return message;
    }
}
