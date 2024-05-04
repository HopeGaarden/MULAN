package com.example.back.api.disease.service;

import com.example.back.config.IntegrationHelper;
import com.example.back.domain.auth.disease.DiseaseInfo;
import com.example.back.domain.auth.disease.DiseaseMember;
import com.example.back.domain.auth.disease.repository.DiseaseInfoRepository;
import com.example.back.domain.auth.disease.repository.DiseaseMemberRepository;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.auth.member.repository.MemberRepository;
import com.example.back.domain.wiki.DiseaseWiki;
import com.example.back.domain.wiki.repository.DiseaseWikiRepository;
import com.example.back.global.exception.DiseaseMemberException;
import com.example.back.global.exception.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.back.domain.auth.MemberFixture.새로운_유저_생성;
import static com.example.back.domain.auth.MemberFixture.일반_유저_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class DiseaseMemberServiceTest extends IntegrationHelper {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    DiseaseInfoRepository diseaseInfoRepository;

    @Autowired
    DiseaseMemberRepository diseaseMemberRepository;

    @Autowired
    DiseaseWikiRepository diseaseWikiRepository;

    @Autowired
    DiseaseMemberService diseaseMemberService;

    private Member member;
    private Member otherMember;
    private DiseaseInfo disease;
    private DiseaseInfo otherDisease;
    private DiseaseMember diseaseMember;
    private DiseaseMember otherDiseaseMember;
    private DiseaseWiki diseaseWiki;


    @BeforeEach
    void setup() {
        member =  memberRepository.save(일반_유저_생성());
        otherMember = memberRepository.save(새로운_유저_생성());
        disease = diseaseInfoRepository.save(DiseaseInfo.builder()
                .code("A").name("A 증후군").build());
        otherDisease = diseaseInfoRepository.save(DiseaseInfo.builder()
                .code("B").name("B 증후군").build());
        diseaseMember = diseaseMemberRepository.save(DiseaseMember.builder()
                .diseaseInfo(disease).member(member).build());
        otherDiseaseMember = diseaseMemberRepository.save(DiseaseMember.builder()
                .diseaseInfo(otherDisease).member(otherMember).build());
        diseaseWiki = diseaseWikiRepository.save(DiseaseWiki.builder()
                .content("테스트").diseaseInfoId(disease.getId()).authorId(member.getId()).build());
    }

    @Test
    void 특정_질병에_해당하는_사용자일_경우_성공_테스트() {
        diseaseMemberService.isSpecificDiseaseMember(member, disease.getId());
    }


    @Test
    void 특정_질병에_해당하는_사용자가_아닐_경우_실패_테스트() {
        var e = assertThrows(DiseaseMemberException.class, () -> {
            diseaseMemberService.isSpecificDiseaseMember(otherMember, disease.getId());
        });

        assertEquals(e.getMessage(), ExceptionMessage.DISEASE_MEMBER_NOT_FOUND.getText());

    }

}