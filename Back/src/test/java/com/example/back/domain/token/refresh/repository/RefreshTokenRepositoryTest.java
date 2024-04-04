package com.example.back.domain.token.refresh.repository;

import com.example.back.domain.token.refresh.RefreshToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @AfterEach
    void tearDown() {
        refreshTokenRepository.deleteAll();
    }

    @Test
    @DisplayName("RefreshToken 저장 & 조회 테스트")
    void refreshTokenTest() {
        // given
        RefreshToken saveToken = refreshTokenRepository.save(RefreshToken.builder()
                .refreshToken("testToken")
                .email("email@naver.com")
                .isRevoked(false)
                .build());

        // when
        RefreshToken refreshToken = refreshTokenRepository.findById(saveToken.getRefreshToken()).get();

        // then
        assertThat(refreshToken.getRefreshToken()).isEqualTo(saveToken.getRefreshToken());
        assertThat(refreshToken.getEmail()).isEqualTo(saveToken.getEmail());
    }
}