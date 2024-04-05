package com.example.back.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailConfig {

    @Value("${mail.port}")
    private int MAIL_SERVER_PORT;

    @Value("${mail.host}")
    private String host;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();

        javaMailSender.setHost(host);
        javaMailSender.setUsername(username);
        javaMailSender.setPassword(password);
        javaMailSender.setPort(MAIL_SERVER_PORT);
        javaMailSender.setJavaMailProperties(getMailProperties());

        return javaMailSender;
    }

    // 이메일 속성 설정
    private Properties getMailProperties() {
        Properties properties = new Properties();

        // SMTP 프로토콜 지정
        properties.setProperty("mail.transport.protocol", "smtp");
        // SMTP 서버의 인증을 사용할지 여부
        properties.setProperty("mail.smtp.auth", "true");
        // 보안 연결을 시작할지 여부
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "false");
        // SSL(SMTP로 전송되는 데이터 암호화하는 프로토콜) 연결에 대한 사용 및 신뢰 여부
        properties.setProperty("mail.smtp.ssl.trust", host);
        properties.setProperty("mail.smtp.ssl.enable", "true");

        return properties;
    }
}
