package com.example.back.api.auth.controller.login.response;

import lombok.Builder;

@Builder
public record LoginResponse(
        String accessToken
) {
}
