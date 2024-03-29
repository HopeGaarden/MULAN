package com.example.back.api.auth.controller.response;

import lombok.Builder;

@Builder
public record LoginResponse(
        String accessToken
) {
}
