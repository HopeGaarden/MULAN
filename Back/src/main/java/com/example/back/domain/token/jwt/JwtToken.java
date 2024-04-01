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
    private String email;
    private boolean revoked;

    @Builder
    public JwtToken(String token, boolean revoked, String email) {
        this.token = token;
        this.revoked = revoked;
        this.email = email;
    }

    public void setTokenInvalid() {
        this.revoked = true;
    }
}
