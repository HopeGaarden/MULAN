package com.example.back.api.auth.service.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@NoArgsConstructor
@Component
public class JwtTokenProvider {
    @Value("${jwt.secret-key}")
    private String secret;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private long refreshExpiration;

    /*
        Token 생성
    */
    public String generateToken(Map<String, String> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshExpiration);
    }
    private String buildToken(Map<String, String> extraClaims, UserDetails userDetails, long expiration) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }


    /*
        JWT 토큰 정보 추출
    */
    // 모든 Claim 추출
    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 특정 Claim 추출
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        Claims claims = extractAllClaims(token);

        return claimsResolver.apply(claims);
    }

    // sub 추출
    public String extractSubject(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // 만료 일자 추출
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /*
        JWT 토큰 검증
    */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final Claims claims = extractAllClaims(token);

        if (!claims.containsKey("role")) return false;
        if (!claims.containsKey("nickname")) return false;
        if (!claims.containsKey("password")) return false;

        final String username = extractSubject(token);

        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    public boolean isRefreshTokenValid(String token, UserDetails userDetails) {

        return (extractSubject(token).equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // JWT 토큰이 만료되었는지 확인
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    // JWT 서명에 사용할 키 획득
    private Key getSignInKey() {
        // Base64로 암호화(인코딩)되어 있는 secretKey를 바이트 배열로 복호화(디코딩)
        byte[] keyBytes = Base64.getDecoder().decode(secret);

        // JWT 서명을 위해 HMAC 알고리즘 적용
        return Keys.hmacShaKeyFor(keyBytes);
    }

}