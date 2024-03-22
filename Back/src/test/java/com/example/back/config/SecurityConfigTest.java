package com.example.back.config;

import com.example.back.api.auth.service.jwt.JwtTokenProvider;
import com.example.back.common.exception.ExceptionMessage;
import com.example.back.common.utils.TokenUtil;
import com.example.back.domain.auth.MemberFixture;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.auth.member.repository.MemberRepository;
import com.example.back.domain.token.jwt.JwtToken;
import com.example.back.domain.token.jwt.constant.TokenType;
import com.example.back.domain.token.jwt.repository.TokenRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
class SecurityConfigTest extends IntegrationHelper {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    TokenRepository tokenRepository;

    @Test
    @DisplayName("인증되지 않은 사용자는 허용되지 않은 엔드포인트에 접근할 수 없다.")
    void unAuthUserTest() throws Exception {// given
        String uri = "/test";

        // when
        mockMvc.perform(
                        get(uri)
                )
                // then
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("모든 사용자는 인증 없이 \"/auth/**\" URI에 접근 가능하다.")
    void authUriPermitAllTest() throws Exception {
        // given
        String expectBody = "test!!";
        String uri = "/auth/test";

        // when
        mockMvc.perform(
                        get(uri)
                )
                // then
                .andExpect(status().isOk())
                .andExpect(content().string(expectBody))
                .andDo(print());
    }

    @Test
    void 인증_받은_사용자는_모든_엔드포인트에_접근할_수_있다() throws Exception {
        // given
        String uri = "/test";
        String expectBody = "test!!";

        Member savedMember = memberRepository.save(MemberFixture.일반_유저_생성());
        String jwtToken = jwtTokenProvider.generateToken(TokenUtil.createTokenMap(savedMember), savedMember);

        tokenRepository.save(JwtToken.builder()
                .token(jwtToken)
                .email(savedMember.getEmail())
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build());

        String refreshToken = jwtTokenProvider.generateRefreshToken(savedMember);

        // when
        mockMvc.perform(
                        get(uri)
                                .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(status().isOk())
                .andExpect(content().string(expectBody))
                .andDo(print());
    }

    @Test
    void 더_이상_사용할_수_없는_토큰은_검증에_실패한다() throws Exception {
        // given
        String uri = "/test";
        String expectBody = "test!!";


        Member savedMember = memberRepository.save(MemberFixture.일반_유저_생성());
        String jwtToken = jwtTokenProvider.generateToken(TokenUtil.createTokenMap(savedMember), savedMember);

        tokenRepository.save(JwtToken.builder()
                .token(jwtToken)
                .email(savedMember.getEmail())
                .tokenType(TokenType.BEARER)
                .expired(true)
                .revoked(true)
                .build());

        String refreshToken = jwtTokenProvider.generateRefreshToken(savedMember);

        // when
        mockMvc.perform(
                        get(uri)
                                .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.res_code").value(400))
                .andExpect(jsonPath("$.res_msg").value(ExceptionMessage.JWT_TOKEN_INVALID.getText()))
                .andDo(print());
    }

    @Test
    void 로그아웃한_사용자의_토큰은_더_이상_사용할_수_없다() throws Exception {
        // given
        String uri = "/auth/logout";

        Member savedMember = memberRepository.save(MemberFixture.일반_유저_생성());
        String jwtToken = jwtTokenProvider.generateToken(TokenUtil.createTokenMap(savedMember), savedMember);

        tokenRepository.save(JwtToken.builder()
                .token(jwtToken)
                .email(savedMember.getEmail())
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build());

        // when
        mockMvc.perform(
                        get(uri)
                                .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk());


        // then
        JwtToken findToken = tokenRepository.findByToken(jwtToken).get();
        assertTrue(findToken.isRevoked());
        assertTrue(findToken.isExpired());
    }
}