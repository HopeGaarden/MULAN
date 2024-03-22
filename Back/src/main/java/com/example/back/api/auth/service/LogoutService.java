package com.example.back.api.auth.service;

import com.example.back.api.auth.service.jwt.JwtTokenProvider;
import com.example.back.common.exception.ExceptionMessage;
import com.example.back.common.exception.TokenException;
import com.example.back.common.utils.AuthenticationExtractor;
import com.example.back.domain.token.jwt.JwtToken;
import com.example.back.domain.token.jwt.repository.TokenRepository;
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

    private final TokenRepository tokenRepository;
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

    private void revokeAllUserTokens(String userEmail) {
        List<JwtToken> validTokens = tokenRepository.findAllValidTokenByUserId(userEmail);
        if (!validTokens.isEmpty()) {
            validTokens.forEach(JwtToken::setTokenInvalid);
            tokenRepository.saveAll(validTokens);
        }
    }

}
