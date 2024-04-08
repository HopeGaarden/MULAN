package com.example.back.api.auth.controller;

import com.example.back.api.auth.controller.request.AuthRequest;
import com.example.back.api.auth.controller.request.SignUpRequest;
import com.example.back.api.auth.service.AuthService;
import com.example.back.api.auth.service.jwt.JwtTokenProvider;
import com.example.back.api.auth.service.response.AuthResponse;
import com.example.back.common.utils.TokenUtil;
import com.example.back.config.IntegrationHelper;
import com.example.back.domain.auth.MemberFixture;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.auth.member.repository.MemberRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static com.example.back.config.IntegrationHelper.NON_ASCII;
import static com.example.back.domain.auth.MemberFixture.일반_유저_생성;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SuppressWarnings(NON_ASCII)
@AutoConfigureMockMvc
class AuthControllerTest extends IntegrationHelper {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void 로그인_요청_성공_테스트() throws Exception {
        // given
        Member member = memberRepository.save(일반_유저_생성());

        Map<String, String> map = TokenUtil.createTokenMap(member);
        String token = jwtTokenProvider.generateToken(map, member);
        String refresh = jwtTokenProvider.generateRefreshToken(member);

        // when
        when(authService.authenticate(any(AuthRequest.class)))
                .thenReturn(AuthResponse.builder()
                        .accessToken(token)
                        .refreshToken(refresh)
                        .build());

        // then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AuthRequest.builder()
                                .email("email@naver.com")
                                .password("password")
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").exists())
                .andExpect(header().exists(HttpHeaders.SET_COOKIE))
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refreshToken")))
                .andDo(print());

    }

    @Test
    void 잘못된_이메일_형식으로_로그인_요청시_실패해야_한다() throws Exception {
        // given
        String inValidEmail = "invalid email";
        String expectedValue = "email: 잘못된 형식의 이메일입니다.";

        Member member = memberRepository.save(일반_유저_생성());

        Map<String, String> map = TokenUtil.createTokenMap(member);
        String token = jwtTokenProvider.generateToken(map, member);
        String refresh = jwtTokenProvider.generateRefreshToken(member);

        // when
        when(authService.authenticate(any(AuthRequest.class)))
                .thenReturn(AuthResponse.builder()
                        .accessToken(token)
                        .refreshToken(refresh)
                        .build());

        // then
        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(AuthRequest.builder()
                                .email(inValidEmail)
                                .password("password")
                                .build())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res_code").value(400))
                .andExpect(jsonPath("$.res_msg").value(expectedValue))
                .andDo(print());

    }

    @Test
    void 리프레시_토큰으로_재발급_요청_성공_테스트() throws Exception {
        // given
        Member member = memberRepository.save(일반_유저_생성());

        String refreshToken = jwtTokenProvider.generateRefreshToken(member);
        String newAccessToken = jwtTokenProvider.generateToken(Map.of("email", member.getEmail()), member);

        when(authService.refreshJwtToken(anyString()))
                .thenReturn(AuthResponse.builder()
                        .accessToken(newAccessToken)
                        .refreshToken(refreshToken)
                        .build());

        // then
        mockMvc.perform(post("/auth/refresh")
                        .cookie(new Cookie("refreshToken", refreshToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access_token").value(newAccessToken))
                .andExpect(header().exists(HttpHeaders.SET_COOKIE)) // 쿠키가 잘 설정되었는지 확인
                .andExpect(header().string(HttpHeaders.SET_COOKIE, containsString("refreshToken=")))
                .andDo(print());
    }

    @Test
    void 회원가입_요청_성공_테스트() throws Exception {
        // given
        Member member = MemberFixture.일반_유저_생성();

        SignUpRequest request = SignUpRequest.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password(member.getPassword())
                .passwordVerify(member.getPassword())
                .build();

        // when
        doNothing().when(authService).signUp(any(SignUpRequest.class));

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @Test
    void 회원가입_요청_유효성_검증_실패_테스트_1() throws Exception {
        // given
        Member member = MemberFixture.일반_유저_생성();

        SignUpRequest request = SignUpRequest.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password("@@@@@@@@@@")
                .passwordVerify(member.getPassword())
                .build();

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res_msg").value("password: 비밀번호는 최소한 하나의 대문자, 소문자, 특수문자 및 숫자를 포함해야 합니다."))
                .andDo(print());
    }

    @Test
    void 회원가입_요청_유효성_검증_실패_테스트_2() throws Exception {
        // given
        Member member = MemberFixture.일반_유저_생성();

        SignUpRequest request = SignUpRequest.builder()
                .email(member.getEmail())
                .nickname(member.getNickname())
                .password("Aa@1")
                .passwordVerify(member.getPassword())
                .build();

        mockMvc.perform(post("/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res_msg").value("password: 비밀번호는 8자 이상 20자 이하여야 합니다."))
                .andDo(print());
    }

}