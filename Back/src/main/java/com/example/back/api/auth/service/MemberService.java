package com.example.back.api.auth.service;

import com.example.back.domain.auth.member.Member;
import com.example.back.domain.auth.member.repository.MemberRepository;
import com.example.back.global.exception.ExceptionMessage;
import com.example.back.global.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    //맴버 검증 메서드
    public Member memberValidation(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(() -> {
            log.error("[BR ERROR]: {}", ExceptionMessage.MEMBER_NOT_FOUND.getText());
            return new MemberException(ExceptionMessage.MEMBER_NOT_FOUND);
        });
    }
}
