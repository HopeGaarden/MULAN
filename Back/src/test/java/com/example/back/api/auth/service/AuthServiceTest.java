package com.example.back.api.auth.service;

import com.example.back.api.auth.controller.request.AuthRequest;
import com.example.back.api.auth.controller.request.SignUpRequest;
import com.example.back.api.auth.service.jwt.JwtTokenProvider;
import com.example.back.api.auth.service.response.AuthResponse;
import com.example.back.config.IntegrationHelper;
import com.example.back.domain.auth.MemberFixture;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.auth.member.repository.MemberRepository;
import com.example.back.domain.token.jwt.repository.JwtTokenRepository;
import com.example.back.domain.token.refresh.RefreshToken;
import com.example.back.domain.token.refresh.repository.RefreshTokenRepository;
import com.example.back.global.exception.ExceptionMessage;
import com.example.back.global.exception.MemberException;
import com.example.back.global.exception.SignUpException;
import com.example.back.global.exception.TokenException;
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
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.*;

import static com.example.back.config.IntegrationHelper.NON_ASCII;
import static com.example.back.domain.auth.MemberFixture.일반_유저_생성;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
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

    @Autowired
    PasswordEncoder passwordEncoder;

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


    @Test
    void 회원가입_성공_테스트() {
        // given
        Member member = MemberFixture.새로운_유저_생성();

        var request = SignUpRequest.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .passwordVerify(member.getPassword())
                .build();

        // when
        authService.signUp(request);

        Member findMember = memberRepository.findByEmail(member.getEmail()).get();

        // then
        assertSoftly(softly -> {
            softly.assertThat(findMember).isNotNull();
            softly.assertThat(findMember.getEmail()).isEqualTo(request.email());
            softly.assertThat(passwordEncoder.matches(request.password(), findMember.getPassword())).isTrue();
        });
    }

    @Test
    void 중복된_이메일로_회원가입_요청시_실패해야_한다() {
        // given
        var request = SignUpRequest.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .passwordVerify(member.getPassword())
                .build();

        // when
        var e = assertThrows(MemberException.class, () -> {
            authService.signUp(request);
        });

        assertEquals(ExceptionMessage.MEMBER_EMAIL_ALREADY_EXIST.getText(), e.getMessage());

    }

    @Test
    void 입력된_패스워드가_서로_일치하지_않으면_회원가입에_실패해야_한다() {
        // given
        Member member = MemberFixture.새로운_유저_생성();

        var request = SignUpRequest.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .passwordVerify(member.getPassword() + " ")
                .build();

        // when
        var e = assertThrows(SignUpException.class, () -> {
            authService.signUp(request);
        });

        assertEquals(ExceptionMessage.MEMBER_PASSWORD_DO_NOT_MATCH.getText(), e.getMessage());

    }

    @Test
    void 동일_이메일로_동시에_회원가입_시도시_처리_검증() throws Exception {
        // given
        Member member = MemberFixture.새로운_유저_생성();

        var requestA = SignUpRequest.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .passwordVerify(member.getPassword())
                .build();

        var requestB = SignUpRequest.builder()
                .email(member.getEmail())
                .nickname("T" + member.getNickname())
                .password(member.getPassword())
                .passwordVerify(member.getPassword())
                .build();

        // when
        ExecutorService executor = Executors.newFixedThreadPool(2);
        Future<Boolean> future1 = executor.submit(createSignUpCallable(requestA));
        Future<Boolean> future2 = executor.submit(createSignUpCallable(requestB));
        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        // then
        boolean success1 = future1.get(); // 첫 번째 요청의 결과
        boolean success2 = future2.get(); // 두 번째 요청의 결과

        assertTrue((success1 || success2) && !(success1 && success2)
                , "정확히 하나의 회원가입 요청만 성공해야 한다.");

    }

    private Callable<Boolean> createSignUpCallable(SignUpRequest request) {
        return () -> {
            try {
                authService.signUp(request);
                return true;
            } catch (Exception e) {
                return false; // 실패 시 false 반환
            }
        };
    }
}