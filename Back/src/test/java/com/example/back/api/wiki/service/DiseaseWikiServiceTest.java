package com.example.back.api.wiki.service;

import com.example.back.api.wiki.controller.request.DiseaseWikiPatchRequest;
import com.example.back.config.IntegrationHelper;
import com.example.back.domain.auth.disease.DiseaseInfo;
import com.example.back.domain.auth.disease.DiseaseMember;
import com.example.back.domain.auth.disease.repository.DiseaseInfoRepository;
import com.example.back.domain.auth.disease.repository.DiseaseMemberRepository;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.auth.member.repository.MemberRepository;
import com.example.back.domain.wiki.DiseaseWiki;
import com.example.back.domain.wiki.repository.DiseaseWikiRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class DiseaseWikiServiceTest extends IntegrationHelper {

    @Autowired
    private DiseaseMemberRepository diseaseMemberRepository;

    @Autowired
    private DiseaseWikiRepository diseaseWikiRepository;

    @Autowired
    private DiseaseInfoRepository diseaseInfoRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DiseaseWikiService diseaseWikiService;

    @Test
    public void testSaveWiki_Success() {
        // given
        Member member = memberRepository.save(Member.builder().email("hello@naver.com").nickname("hello").password("hello").build());

        DiseaseInfo disease = diseaseInfoRepository.save(DiseaseInfo.builder().code("A").name("A 증후군").build());

        DiseaseMember diseaseMember = diseaseMemberRepository.save(DiseaseMember.builder().diseaseInfo(disease).member(member).build());

        DiseaseWiki witi = diseaseWikiRepository.save(DiseaseWiki.builder().diseaseInfoId(disease.getId()).authorId(100000L).content("test").build());

        DiseaseWikiPatchRequest request = DiseaseWikiPatchRequest.builder()
                .DiseaseWikiId(witi.getId())
                .content("안녕안녕")
                .build();

        // when
        diseaseWikiService.patchWiki(witi.getId(), diseaseMember, request);

        // then
        DiseaseWiki diseaseWiki = diseaseWikiRepository.findByDiseaseInfoId(disease.getId()).get();
        System.out.println("diseaseWiki = " + diseaseWiki.getContent());
        System.out.println("diseaseWiki.getAuthor() = " + diseaseWiki.getAuthorId());

    }
}