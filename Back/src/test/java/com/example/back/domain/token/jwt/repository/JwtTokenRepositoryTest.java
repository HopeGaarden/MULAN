package com.example.back.domain.token.jwt.repository;

import com.example.back.domain.token.jwt.JwtToken;
import com.example.back.domain.token.refresh.RefreshToken;
import com.example.back.domain.token.refresh.repository.RefreshTokenRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class JwtTokenRepositoryTest {

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    @AfterEach
    void tearDown() {
        jwtTokenRepository.deleteAll();
    }

    @Test
    @DisplayName("JwtToken 저장 & 조회 테스트")
    void JwtTokenTest() {
        // given
        JwtToken saveToken = jwtTokenRepository.save(JwtToken.builder()
                .token("testToken")
                .expired(false)
                .email("email@naver.com")
                .build());

        // when
        JwtToken jwtToken = jwtTokenRepository.findById(saveToken.getToken()).get();

        // then
        assertThat(jwtToken.getToken()).isEqualTo(saveToken.getToken());
        assertThat(jwtToken.getEmail()).isEqualTo(saveToken.getEmail());

        // when
        jwtToken.setTokenInvalid();
        JwtToken expiredToken = jwtTokenRepository.save(jwtToken);

        // then
        assertTrue(expiredToken.isExpired());
    }

    @Test
    @DisplayName("email로 만료된 토큰 조회")
    void selectValidTokenTest() {
        // given
        String email = "email@naver.com";

        JwtToken tokenA = jwtTokenRepository.save(JwtToken.builder()
                .token("tokenA")
                .expired(false)
                .email(email)
                .build());

        JwtToken tokenB = jwtTokenRepository.save(JwtToken.builder()
                .token("tokenB")
                .expired(false)
                .email(email)
                .build());

        JwtToken tokenC = jwtTokenRepository.save(JwtToken.builder()
                .token("tokenC")
                .expired(true)
                .email(email)
                .build());

        // when
        List<JwtToken> list = jwtTokenRepository.findByEmailAndExpiredIsFalse(email);
//        System.out.println("list.size() = " + list.size());
//        for (var t : list) {
//            System.out.println("t.getToken() = " + t.getToken());
//        }

        assertSoftly(softly -> {
            softly.assertThat(list).hasSize(2);
            softly.assertThat(list.get(0).getToken()).isEqualTo(tokenA.getToken());
            softly.assertThat(list.get(1).getToken()).isEqualTo(tokenB.getToken());

        });

    }

}