package com.example.back.api.auth.service;

import com.example.back.api.auth.controller.request.AuthRequest;
import com.example.back.api.auth.service.jwt.JwtTokenProvider;
import com.example.back.api.auth.service.response.AuthResponse;
import com.example.back.common.exception.ExceptionMessage;
import com.example.back.common.exception.MemberException;
import com.example.back.config.IntegrationHelper;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.auth.member.repository.MemberRepository;
import com.example.back.domain.token.jwt.repository.JwtTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;

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
    JwtTokenProvider jwtTokenProvider;

    private Member member;

    @BeforeEach
    void setup() {
        member = 일반_유저_생성();
        memberRepository.save(member);
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
}