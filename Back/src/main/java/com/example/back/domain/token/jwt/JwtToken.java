package com.example.back.domain.token.jwt;

import com.example.back.domain.BaseEntity;
import com.example.back.domain.token.jwt.constant.TokenType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name="TOKEN")
@Entity
public class JwtToken extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 500)
    private String token;

    @Enumerated(EnumType.STRING)
    private TokenType tokenType;

    private boolean expired;

    private boolean revoked;

    private String email;

    @Builder
    public JwtToken(String token, TokenType tokenType, boolean expired, boolean revoked, String email) {
        this.token = token;
        this.tokenType = tokenType;
        this.expired = expired;
        this.revoked = revoked;
        this.email = email;
    }

    public void setTokenInvalid() {
        this.expired = true;
        this.revoked = true;
    }
}
