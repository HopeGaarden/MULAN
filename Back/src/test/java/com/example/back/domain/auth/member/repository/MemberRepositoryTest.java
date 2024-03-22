package com.example.back.domain.auth.member.repository;

import com.example.back.config.IntegrationHelper;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.auth.member.MemberFixture;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.example.back.domain.auth.member.MemberFixture.일반_유저_생성;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.junit.jupiter.api.Assertions.*;


@SuppressWarnings("NonAsciiCharacters")
@ActiveProfiles("test")
@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    void afterEach() {
        memberRepository.deleteAllInBatch();
    }

    @Test
    void 아이디_값으로_멤버를_찾는다() {
        // given
        Member member = memberRepository.save(MemberFixture.일반_유저_생성());

        // when
        Optional<Member> result = memberRepository.findById(member.getId());
        System.out.println("result = " + result);

        // then
        assertSoftly(softly -> {
            softly.assertThat(result).isPresent();
            softly.assertThat(result.get()).usingRecursiveComparison().isEqualTo(member);
        });
    }
}
