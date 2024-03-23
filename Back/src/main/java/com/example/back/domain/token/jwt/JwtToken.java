package com.example.back.domain.token.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@ToString
@NoArgsConstructor
@RedisHash(value = "jwt", timeToLive = 60*60*24) // 1일
public class JwtToken {
    @Id
    private String token;

    @Indexed
    private boolean expired;

    @Indexed
    private String email;

    @Builder
    public JwtToken(String token, boolean expired, String email) {
        this.token = token;
        this.expired = expired;
        this.email = email;
    }

    public void setTokenInvalid() {
        this.expired = true;
    }
}
