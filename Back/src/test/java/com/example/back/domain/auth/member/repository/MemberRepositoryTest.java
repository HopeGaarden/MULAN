package com.example.back.domain.auth.member.repository;

import com.example.back.config.IntegrationHelper;
import com.example.back.domain.auth.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static com.example.back.domain.auth.member.MemberFixture.일반_유저_생성;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("NonAsciiCharacters")
class MemberRepositoryTest extends IntegrationHelper {

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    @BeforeEach
    void setup() {
        member = 일반_유저_생성();
        System.out.println("member.getEmail() = " + member.getEmail());
        memberRepository.save(member);
    }

    @Test
    void 아이디_값으로_멤버를_찾는다() {
        // when
        Optional<Member> result = memberRepository.findById(member.getId());
        System.out.println("result = " + result);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isPresent();
            softly.assertThat(result.get()).usingRecursiveComparison().isEqualTo(member);
        });
    }

    @Test
    void 닉네임_값으로_멤버를_찾는다() {
        // when
        Optional<Member> result = memberRepository.findByNickname(member.getNickname());
        System.out.println("result = " + result);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isPresent();
            softly.assertThat(result.get()).usingRecursiveComparison().isEqualTo(member);
        });
    }

    @Test
    void 이메일_값으로_멤버를_찾는다() {
        // when
        Optional<Member> result = memberRepository.findByEmail(member.getEmail());
        System.out.println("result = " + result);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isPresent();
            softly.assertThat(result.get()).usingRecursiveComparison().isEqualTo(member);
        });
    }
}
