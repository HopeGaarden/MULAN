package com.example.back.common.utils;

import lombok.Builder;

import java.util.List;

@Builder
public record DiffDTO(
        int position,
        String originalLine,
        String newLine
) {}
