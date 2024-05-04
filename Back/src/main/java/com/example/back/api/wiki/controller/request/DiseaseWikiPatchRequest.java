package com.example.back.api.wiki.controller.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record DiseaseWikiPatchRequest(
        Long DiseaseWikiId,
        @NotBlank(message = "질병 백과의 내용은 공백일 수 없습니다.")
        String content
){

}
