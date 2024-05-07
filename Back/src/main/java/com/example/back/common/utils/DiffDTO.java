package com.example.back.common.utils;

import lombok.Builder;

import java.util.List;

@Builder
public record DiffDTO(
        int originalPosition,
        int newPosition,
        List<String> originalLines,
        List<String> newLines
) {}
