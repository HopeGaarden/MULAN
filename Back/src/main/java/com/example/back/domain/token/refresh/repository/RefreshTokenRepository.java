package com.example.back.domain.token.refresh.repository;


import com.example.back.domain.token.refresh.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
