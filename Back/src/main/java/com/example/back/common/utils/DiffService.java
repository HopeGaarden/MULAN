package com.example.back.common.utils;

import java.util.List;
import java.util.Arrays;

import com.example.back.global.exception.DiseaseWikiException;
import com.example.back.global.exception.ExceptionMessage;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.DiffException;
import com.github.difflib.patch.Patch;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DiffService {
    public static List<DiffDTO> calculateDiff(String originalText, String newText) {
        List<String> originalLines = Arrays.asList(originalText.split("\n"));
        List<String> newLines = Arrays.asList(newText.split("\n"));

        // DiffUtils를 사용하여 두 텍스트 리스트 사이의 차이점 계산
        try {
            Patch<String> patch = DiffUtils.diff(originalLines, newLines);
            return patch.getDeltas().stream()
                    .map(delta -> DiffDTO.builder()
                            .originalPosition(delta.getSource().getPosition())
                            .newPosition(delta.getTarget().getPosition())
                            .originalLines(delta.getSource().getLines())
                            .newLines(delta.getTarget().getLines())
                            .build())
                    .toList();
        } catch (Exception e) {
            log.error("[BR ERROR] : {}", ExceptionMessage.WIKI_DIFF_EXCEPTION.getText());
            throw new DiseaseWikiException(ExceptionMessage.WIKI_DIFF_EXCEPTION);
        }

    }
}
