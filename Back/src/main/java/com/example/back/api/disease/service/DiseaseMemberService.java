package com.example.back.api.disease.service;

import com.example.back.domain.auth.disease.DiseaseMember;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.auth.member.repository.MemberRepository;
import com.example.back.domain.auth.disease.repository.DiseaseMemberRepository;
import com.example.back.domain.wiki.DiseaseWiki;
import com.example.back.domain.wiki.repository.DiseaseWikiRepository;
import com.example.back.global.exception.DiseaseWikiException;
import com.example.back.global.exception.ExceptionMessage;
import com.example.back.global.exception.DiseaseMemberException;
import com.example.back.global.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class DiseaseMemberService {
    private final MemberRepository memberRepository;
    private final DiseaseMemberRepository diseaseMemberRepository;
    private final DiseaseWikiRepository diseaseWikiRepository;

    public DiseaseMember isDiseaseMember(Member contextMember) {
        Member member = memberRepository.findByEmail(contextMember.getEmail()).orElseThrow(() -> {
            log.error("[BR ERROR]: {}", ExceptionMessage.MEMBER_NOT_FOUND.getText());
            throw new MemberException(ExceptionMessage.MEMBER_NOT_FOUND);
        });

        return diseaseMemberRepository.findByMember(member).orElseThrow(() -> {
            log.error("[BR ERROR]: {}", ExceptionMessage.DISEASE_MEMBER_NOT_FOUND.getText());
            throw new DiseaseMemberException(ExceptionMessage.DISEASE_MEMBER_NOT_FOUND);
        });
    }

    public DiseaseMember isSpecificDiseaseMember(Member contextMember, Long diseaseWikiId) {
        Member member = memberRepository.findByEmail(contextMember.getEmail()).orElseThrow(() -> {
            log.error("[BR ERROR]: {}", ExceptionMessage.MEMBER_NOT_FOUND.getText());
            throw new MemberException(ExceptionMessage.MEMBER_NOT_FOUND);
        });

        DiseaseWiki wiki = diseaseWikiRepository.findById(diseaseWikiId).orElseThrow(() -> {
            log.error("[BR ERROR]: {}", ExceptionMessage.WIKI_NOT_FOUND.getText());
            throw new DiseaseWikiException(ExceptionMessage.WIKI_NOT_FOUND);
        });

        return diseaseMemberRepository.findByMemberAndDiseaseInfoId(member, wiki.getDiseaseInfoId()).orElseThrow(() -> {
            log.error("[BR ERROR]: {}", ExceptionMessage.DISEASE_MEMBER_NOT_FOUND.getText());
            throw new DiseaseMemberException(ExceptionMessage.DISEASE_MEMBER_NOT_FOUND);
        });
    }

}
