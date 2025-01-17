package com.example.back.domain.token.jwt.repository;

import com.example.back.domain.token.jwt.JwtToken;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JwtTokenRepository extends CrudRepository<JwtToken, String> {
    List<JwtToken> findByEmail(String email);
}
