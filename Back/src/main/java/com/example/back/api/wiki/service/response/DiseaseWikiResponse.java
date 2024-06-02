package com.example.back.api.wiki.service.response;

import com.example.back.domain.wiki.DiseaseWiki;
import lombok.Builder;

@Builder
public record DiseaseWikiResponse (
    Long diseaseInfoId,
    String content,
    int version
) {
    public static DiseaseWikiResponse from(DiseaseWiki diseaseWiki) {
        return DiseaseWikiResponse.builder()
                .diseaseInfoId(diseaseWiki.getDiseaseInfoId())
                .content(diseaseWiki.getContent())
                .version(diseaseWiki.getVersion())
                .build();
    }
}
