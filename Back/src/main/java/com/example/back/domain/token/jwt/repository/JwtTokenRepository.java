package com.example.back.domain.token.jwt.repository;

import com.example.back.domain.token.jwt.JwtToken;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JwtTokenRepository extends CrudRepository<JwtToken, String> {
    // email에 해당하는 토큰 중 활성화된 토큰 리스트 조회
    List<JwtToken> findByEmailAndExpiredIsFalse(String email);
}
