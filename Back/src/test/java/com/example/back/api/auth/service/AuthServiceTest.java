package com.example.back.api.auth.service;

import com.example.back.api.auth.controller.request.AuthRequest;
import com.example.back.api.auth.service.jwt.JwtTokenProvider;
import com.example.back.api.auth.service.response.AuthResponse;
import com.example.back.global.exception.ExceptionMessage;
import com.example.back.global.exception.MemberException;
import com.example.back.global.exception.TokenException;
import com.example.back.config.IntegrationHelper;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.auth.member.repository.MemberRepository;
import com.example.back.domain.token.jwt.repository.JwtTokenRepository;
import com.example.back.domain.token.refresh.RefreshToken;
import com.example.back.domain.token.refresh.repository.RefreshTokenRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

import static com.example.back.config.IntegrationHelper.NON_ASCII;
import static com.example.back.domain.auth.MemberFixture.일반_유저_생성;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings(NON_ASCII)
class AuthServiceTest extends IntegrationHelper {

    @MockBean
    AuthenticationManager authenticationManager;

    @Autowired
    AuthService authService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtTokenRepository jwtTokenRepository;

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Value("${jwt.secret-key}")
    private String secret;

    private Member member;

    @BeforeEach
    void setup() {
        member = 일반_유저_생성();
        memberRepository.save(member);
    }

    @AfterEach
    void tearDown() {
        jwtTokenRepository.deleteAll();
        refreshTokenRepository.deleteAll();
    }

    @Test
    void 로그인_성공_테스트() {
        // given
        AuthRequest login = AuthRequest.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .build();

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenReturn(any(Authentication.class));

        // when
        AuthResponse response = authService.authenticate(login);

        // then
        assertNotNull(response);
        assertNotNull(response.accessToken());
        assertNotNull(response.refreshToken());

    }

    @Test
    void 로그인_실패_테스트() {
        // given
        AuthRequest login = AuthRequest.builder()
                .email(member.getEmail())
                .password(member.getPassword())
                .build();

        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new MemberException(ExceptionMessage.MEMBER_NOT_FOUND));

        // when & then
        MemberException exception = assertThrows(MemberException.class, () -> {
            authService.authenticate(login);
        });
        assertEquals(exception.getMessage(), ExceptionMessage.MEMBER_NOT_FOUND.getText());
    }

    @Test
    void 유효한_리프레시_토큰으로_재발급_요청시_성공() {
        // given
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);

        RefreshToken token = RefreshToken.builder()
                .refreshToken(refreshToken)
                .email(member.getEmail())
                .isRevoked(false)
                .build();
        refreshTokenRepository.save(token);

        // when
        AuthResponse response = authService.refreshJwtToken(refreshToken);

        String newJwtToken = response.accessToken();

        // then
        assertNotNull(newJwtToken);
        assertTrue(jwtTokenProvider.isTokenValid(newJwtToken, member));
    }

    @Test
    void null값의_리프레시_토큰으로_재발급_요청시_실패() {
        // given
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);

        // when
        // then
        TokenException exception = assertThrows(TokenException.class, () -> {
            authService.refreshJwtToken(null);
        });
        assertEquals(ExceptionMessage.REFRESH_TOKEN_IS_NULL.getText(), exception.getMessage());
    }

    @Test
    void 만료된_리프레시_토큰으로_재발급_요청시_실패() {
        // given
        long currentTime = System.currentTimeMillis();
        byte[] keyBytes = Base64.getDecoder().decode(secret);

        Key key = Keys.hmacShaKeyFor(keyBytes);

        String refreshToken = Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(member.getEmail())
                .setIssuedAt(new Date(currentTime))
                .setExpiration(new Date(currentTime - 10))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // when
        assertThrows(ExpiredJwtException.class, () -> {
            authService.refreshJwtToken(refreshToken);
        });
    }

    @Test
    void 존재하지_않는_리프레시_토큰으로_재발급_요청시_실패() {
        // given
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);

//        RefreshToken token = RefreshToken.builder()
//                .refreshToken(refreshToken)
//                .email(member.getEmail())
//                .isRevoked(false)
//                .build();
//        refreshTokenRepository.save(token);

        // when
        TokenException exception = assertThrows(TokenException.class, () -> {
            authService.refreshJwtToken(refreshToken);
        });
        assertEquals(ExceptionMessage.REFRESH_TOKEN_NOT_FOUND.getText(), exception.getMessage());
    }

    @Test
    void 존재하지_않는_사용자로_재발급_요청시_실패() {
        Member user = Member.builder()
                .email("aa@naver.com")
                .password("aa")
                .nickname("aa")
                .build();

        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        RefreshToken token = RefreshToken.builder()
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .isRevoked(false)
                .build();
        refreshTokenRepository.save(token);

        // when
        MemberException exception = assertThrows(MemberException.class, () -> {
            authService.refreshJwtToken(refreshToken);
        });
        assertEquals(ExceptionMessage.MEMBER_NOT_FOUND.getText(), exception.getMessage());

    }

    @Test
    void 철회된_리프레시_토큰으로_재발급_요청시_실패() {
        // given
        String refreshToken = jwtTokenProvider.generateRefreshToken(member);
        RefreshToken token = RefreshToken.builder()
                .refreshToken(refreshToken)
                .email(member.getEmail())
                .isRevoked(true)
                .build();
        refreshTokenRepository.save(token);

        // when & then
        TokenException exception = assertThrows(TokenException.class, () -> authService.refreshJwtToken(refreshToken));
        assertEquals(ExceptionMessage.REFRESH_TOKEN_REVOKED.getText(), exception.getMessage());
    }

}