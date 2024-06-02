package com.example.back.common.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;

import com.example.back.global.exception.DiseaseWikiException;
import com.example.back.global.exception.ExceptionMessage;
import com.github.difflib.DiffUtils;
import com.github.difflib.patch.AbstractDelta;
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
            List<DiffDTO> diffs = new ArrayList<>();

            for (AbstractDelta<String> delta : patch.getDeltas()) {
                List<String> originalDeltaLines = delta.getSource().getLines();
                List<String> newDeltaLines = delta.getTarget().getLines();
                int position = delta.getSource().getPosition();

                int maxLines = Math.max(originalDeltaLines.size(), newDeltaLines.size());

                for (int i = 0; i < maxLines; i++) {
                    String originalLine = i < originalDeltaLines.size() ? originalDeltaLines.get(i) : "";
                    String newLine = i < newDeltaLines.size() ? newDeltaLines.get(i) : "";

                    if (!originalLine.equals(newLine)) {
                        diffs.add(DiffDTO.builder()
                                .position(position + i)
                                .originalLine(originalLine)
                                .newLine(newLine)
                                .build());
                    }
                }
            }
            return diffs;
        } catch (Exception e) {
            log.error("[BR ERROR] : {}", ExceptionMessage.WIKI_DIFF_EXCEPTION.getText());
            throw new DiseaseWikiException(ExceptionMessage.WIKI_DIFF_EXCEPTION);
        }
    }
}


