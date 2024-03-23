package com.example.back.domain.token.refresh;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@ToString
@NoArgsConstructor
@RedisHash(value = "refresh", timeToLive = 60*60*24*7) // 7일
public class RefreshToken {
    @Id
    private String refreshToken;
    private String email;

    @Builder
    public RefreshToken(String refreshToken, String email) {
        this.refreshToken = refreshToken;
        this.email = email;
    }
}
