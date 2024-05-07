package com.example.back.api.wiki.service.response;

import com.example.back.common.utils.DiffDTO;
import lombok.Builder;

import java.util.List;

@Builder
public record WikiResolveConflictResponse(
        String originalContent,
        String newContent,
        List<DiffDTO> diffs
) {}
