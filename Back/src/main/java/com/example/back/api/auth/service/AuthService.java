package com.example.back.api.auth.service;

import com.example.back.api.auth.controller.request.AuthRequest;
import com.example.back.api.auth.service.jwt.JwtTokenProvider;
import com.example.back.api.auth.service.response.AuthResponse;
import com.example.back.common.exception.ExceptionMessage;
import com.example.back.common.exception.MemberException;
import com.example.back.common.exception.TokenException;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.auth.member.repository.MemberRepository;
import com.example.back.domain.token.jwt.JwtToken;
import com.example.back.domain.token.jwt.repository.JwtTokenRepository;
import com.example.back.domain.token.refresh.RefreshToken;
import com.example.back.domain.token.refresh.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private static final String ROLE_CLAIM = "role";
    private static final String NICKNAME_CLAIM = "nickname";

    private final AuthenticationManager authenticationManager;
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final JwtTokenRepository jwtTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public AuthResponse authenticate(AuthRequest login) {
        Member member = memberRepository.findByEmail(login.email()).orElseThrow(() -> {
            log.error("[BR ERROR]: {}", ExceptionMessage.MEMBER_NOT_FOUND.getText());
            throw new MemberException(ExceptionMessage.MEMBER_NOT_FOUND);
        });

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                login.email(),
                login.password()
        ));

        log.info("[BR INFO]: {} 남이 로그인에 성공하셨습니다.", member.getNickname());

        // 이전의 모든 JWT 토큰 사용 불가능하도록 업데이트
        revokeAllUserTokens(member);

        // JWT 토큰 생성
        String jwtToken = generateAndSaveJwtToken(member);

        // refresh token 생성
        String refreshToken = generateAndSaveRefreshToken(member);

        return AuthResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    private void revokeAllUserTokens(Member member) {
        // email에 해당하는 토큰 중 활성화되어 있는 토큰 리스트 조회
        List<JwtToken> validTokens = jwtTokenRepository.findByEmail(member.getEmail()).stream()
                .filter(token -> !token.isRevoked())
                .toList();

        if (!validTokens.isEmpty()) {
            validTokens.forEach(JwtToken::setTokenInvalid);
            jwtTokenRepository.saveAll(validTokens);
        }
    }

    private static HashMap<String, String> setClaims(Member member) {
        HashMap<String, String> claims = new HashMap<>();
        claims.put(ROLE_CLAIM, member.getRole().name());
        claims.put(NICKNAME_CLAIM, member.getNickname());
        return claims;
    }

    private String generateAndSaveJwtToken(Member member) {
        String jwtToken = jwtTokenProvider.generateToken(setClaims(member), member);

        JwtToken token = JwtToken.builder()
                .token(jwtToken)
                .revoked(false)
                .email(member.getEmail())
                .build();

        jwtTokenRepository.save(token);
        return jwtToken;
    }

    private String generateAndSaveRefreshToken(Member member) {
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);

        RefreshToken token = RefreshToken.builder()
                .refreshToken(refreshToken)
                .revoked(false)
                .email(member.getEmail())
                .build();

        refreshTokenRepository.save(token);
        return refreshToken;
    }

}