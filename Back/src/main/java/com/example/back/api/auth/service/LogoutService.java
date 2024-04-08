package com.example.back.api.auth.service;

import com.example.back.api.auth.service.jwt.JwtTokenProvider;
import com.example.back.global.exception.ExceptionMessage;
import com.example.back.global.exception.TokenException;
import com.example.back.common.utils.AuthenticationExtractor;
import com.example.back.domain.token.jwt.JwtToken;
import com.example.back.domain.token.jwt.repository.JwtTokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final JwtTokenRepository jwtTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        String jwtToken = AuthenticationExtractor.extractToken(request)
                .orElseThrow(() -> {
                    log.error("[BR ERROR]: {}", ExceptionMessage.JWT_NOT_FOUND.getText());
                    return new TokenException(ExceptionMessage.JWT_NOT_FOUND);
                });

        String subject = jwtTokenProvider.extractSubject(jwtToken);
        revokeAllUserTokens(subject);

        log.info("[BR INFO]: {} 님이 로그아웃하셨습니다.", subject);

    }

    // 해당 사용자의 토큰들을 전부 비활성화
    private void revokeAllUserTokens(String userEmail) {
        // email에 해당하는 토큰 중 활성화되어 있는 토큰 리스트 조회
        List<JwtToken> validTokens = jwtTokenRepository.findByEmail(userEmail).stream()
                .filter(token -> !token.isRevoked())
                .toList();

        if (!validTokens.isEmpty()) {
            validTokens.forEach(JwtToken::setTokenInvalid);
            jwtTokenRepository.saveAll(validTokens);
        }
    }

}
