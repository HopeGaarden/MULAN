package com.example.back.api.auth.service.response;

import lombok.Builder;

@Builder
public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}