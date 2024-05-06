package com.example.back.api.wiki.service;

import com.example.back.api.wiki.controller.request.DiseaseWikiPatchRequest;
import com.example.back.api.wiki.controller.request.DiseaseWikiSaveRequest;
import com.example.back.api.wiki.service.response.DiseaseWikiResponse;
import com.example.back.domain.auth.disease.DiseaseInfo;
import com.example.back.domain.auth.disease.DiseaseMember;
import com.example.back.domain.wiki.DiseaseWiki;
import com.example.back.domain.wiki.repository.DiseaseWikiRepository;
import com.example.back.global.exception.DiseaseWikiException;
import com.example.back.global.exception.ExceptionMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class DiseaseWikiService {
    private final DiseaseWikiRepository diseaseWikiRepository;

    public List<DiseaseWikiResponse> findAllWikis() {
        return diseaseWikiRepository.findAll().stream()
                .map(DiseaseWikiResponse::from)
                .toList();
    }

    public DiseaseWikiResponse findWiki(Long wikiId) {
        DiseaseWiki diseaseWiki = diseaseWikiRepository.findById(wikiId).orElseThrow(() -> {
            log.error("[BR ERROR] {} : {}", wikiId, ExceptionMessage.WIKI_NOT_FOUND.getText());
            throw new DiseaseWikiException(ExceptionMessage.WIKI_NOT_FOUND);
        });

        return DiseaseWikiResponse.from(diseaseWiki);
    }

    @Transactional
    public void saveWiki(DiseaseMember diseaseMember, String content) {
        DiseaseInfo diseaseInfo = diseaseMember.getDiseaseInfo();

        // 해당 그룹의 위키가 존재하는지 확인
        if (diseaseWikiRepository.existsByDiseaseInfoId(diseaseInfo.getId())) {
            log.error("[BR ERROR] {} : {}", diseaseInfo.getId(), ExceptionMessage.WIKI_ALREADY_EXIST.getText());
            throw new DiseaseWikiException(ExceptionMessage.WIKI_ALREADY_EXIST);
        };

        // 해당 그룹의 질병 정보를 포함해 위키 생성
        diseaseWikiRepository.save(DiseaseWiki.builder()
                .authorId(diseaseMember.getMember().getId())
                .diseaseInfoId(diseaseInfo.getId())
                .content(content)
                .build());

    }

    @Transactional
    public void patchWiki(DiseaseMember diseaseMember, DiseaseWikiPatchRequest request) {
        DiseaseWiki wiki = diseaseWikiRepository.findById(request.DiseaseWikiId()).orElseThrow(() -> {
            log.error("[BR ERROR] {} : {}", request.DiseaseWikiId(), ExceptionMessage.WIKI_NOT_FOUND.getText());
            throw new DiseaseWikiException(ExceptionMessage.WIKI_NOT_FOUND);
        });

        wiki.updateWiki(diseaseMember.getMember().getId(), request.content());
    }
}
