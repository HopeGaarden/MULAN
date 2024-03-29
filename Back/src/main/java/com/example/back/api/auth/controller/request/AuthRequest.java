package com.example.back.api.auth.controller.request;

import com.example.back.common.validation.IsEmail;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record AuthRequest(
        @IsEmail
        @NotBlank(message = "이메일을 입력해주세요.")
        String email,
        @NotBlank(message = "패스워드를 입력해주세요.")
        String password
) {
}
