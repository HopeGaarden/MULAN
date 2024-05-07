package com.example.back.api.wiki.service;

import com.example.back.api.wiki.controller.request.DiseaseWikiPatchRequest;
import com.example.back.api.wiki.service.response.DiseaseWikiResponse;
import com.example.back.config.IntegrationHelper;
import com.example.back.domain.auth.MemberFixture;
import com.example.back.domain.auth.disease.DiseaseInfo;
import com.example.back.domain.auth.disease.DiseaseMember;
import com.example.back.domain.auth.disease.repository.DiseaseInfoRepository;
import com.example.back.domain.auth.disease.repository.DiseaseMemberRepository;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.auth.member.repository.MemberRepository;
import com.example.back.domain.wiki.DiseaseWiki;
import com.example.back.domain.wiki.repository.DiseaseWikiRepository;
import com.example.back.global.exception.DiseaseWikiException;
import com.example.back.global.exception.ExceptionMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;

import static com.example.back.domain.auth.MemberFixture.새로운_유저_생성;
import static com.example.back.domain.auth.MemberFixture.일반_유저_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class DiseaseWikiServiceTest extends IntegrationHelper {
    @Autowired
    MemberRepository memberRepository;

    @Autowired
    DiseaseInfoRepository diseaseInfoRepository;

    @Autowired
    DiseaseMemberRepository diseaseMemberRepository;

    @Autowired
    DiseaseWikiRepository diseaseWikiRepository;

    @Autowired
    DiseaseWikiService diseaseWikiService;

    private Member member;
    private Member otherMember;
    private DiseaseInfo disease;
    private DiseaseInfo otherDisease;
    private DiseaseMember diseaseMember;
    private DiseaseMember otherDiseaseMember;
    private DiseaseWiki diseaseWiki;

    @BeforeEach
    void setup() {
        member = memberRepository.save(일반_유저_생성());
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

    // 전체 조회 성공 테스트
    @Test
    void 전체_조회_성공_테스트() {
        List<DiseaseWikiResponse> allWikis = diseaseWikiService.findAllWikis();

        assertThat(allWikis.size()).isEqualTo(1);
    }

    // 특정 위키 조회 성공 테스트
    @Test
    void 특정_위키_조회_성공_테스트() {
        DiseaseWikiResponse wiki = diseaseWikiService.findWiki(diseaseWiki.getId());

        assertThat(wiki.content()).isEqualTo("테스트");
    }

    // 특정 위키 조회 실패 테스트
    @Test
    void 특정_위키_조회_실패_테스트() {
        var e = assertThrows(DiseaseWikiException.class, () -> {
            diseaseWikiService.findWiki(100000L);
        });

        assertThat(e.getMessage()).isEqualTo(ExceptionMessage.WIKI_NOT_FOUND.getText());
    }

    // 위키 저장 성공 테스트
    @Test
    void 위키_저장_성공_테스트() {
        diseaseWikiService.saveWiki(otherDiseaseMember, "등록테스트");

        DiseaseWiki wiki = diseaseWikiRepository.findByDiseaseInfoId(otherDisease.getId()).get();
        assertThat(wiki.getAuthorId()).isEqualTo(otherMember.getId());
        assertThat(wiki.getContent()).isEqualTo("등록테스트");
    }

    // 위키 저장 실패 테스트
    @Test
    void 위키_저장_실패_테스트() {
        var e = assertThrows(DiseaseWikiException.class, () -> {
            diseaseWikiService.saveWiki(diseaseMember, "등록테스트");
        });

        assertEquals(e.getMessage(), ExceptionMessage.WIKI_ALREADY_EXIST.getText());
    }

    // 위키 수정 성공 테스트
    @Test
    void 위키_수정_성공_테스트() {
        var request = DiseaseWikiPatchRequest.builder().DiseaseWikiId(diseaseWiki.getId()).content("수정테스트").build();

        diseaseWikiService.patchWiki(diseaseMember, request);

        DiseaseWiki findWiki = diseaseWikiRepository.findById(diseaseWiki.getId()).get();

        assertEquals(findWiki.getAuthorId(), member.getId());
        assertEquals(findWiki.getContent(), request.content());

    }

    // 위키 수정 실패 테스트
    @Test
    void 위키_수정_실패_테스트() {
        var request = DiseaseWikiPatchRequest.builder().DiseaseWikiId(10000L).content("수정테스트").build();

        assertThrows(DiseaseWikiException.class, () -> {
            diseaseWikiService.patchWiki(otherDiseaseMember, request);
        });

    }

    @Test
    void 동시에_두_사용자가_수정_요청_시_낙관적_락_테스트() throws InterruptedException {
        Member memberB = memberRepository.save(MemberFixture.새로운_유저_생성B());
        DiseaseMember diseaseMemberB = diseaseMemberRepository.save(DiseaseMember.builder()
                .diseaseInfo(disease).member(memberB).build());

        ExecutorService executor = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        // 첫 번째 사용자의 수정
        executor.submit(() -> {
            try {
                long startTime = System.currentTimeMillis();
                DiseaseWikiPatchRequest request = new DiseaseWikiPatchRequest(diseaseWiki.getId(), "First User Content");
                diseaseWikiService.patchWiki(diseaseMember, request);
                long endTime = System.currentTimeMillis();
                System.out.println("User A start time: " + startTime + " end time: " + endTime);
            } catch (Exception e) {
                System.out.println("Exception for User A: " + e + " message: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        // 두 번째 사용자의 수정
        executor.submit(() -> {
            try {
                long startTime = System.currentTimeMillis();
                DiseaseWikiPatchRequest request = new DiseaseWikiPatchRequest(diseaseWiki.getId(), "Second User Content");
                diseaseWikiService.patchWiki(diseaseMemberB, request);
                long endTime = System.currentTimeMillis();
                System.out.println("User B start time: " + startTime + " end time: " + endTime);
            } catch (Exception e) {
                System.out.println("Exception for User B: " + e + " message: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        });

        latch.await();  // 두 스레드의 완료를 기다림
        executor.shutdown();

        // 최종 결과 확인
        DiseaseWiki result = diseaseWikiRepository.findById(diseaseWiki.getId()).orElseThrow();
        System.out.println("최종 내용: " + result.getContent());
    }

//    @Test
//    void 동시에_두_사용자가_수정_요청_시_비관적_락_테스트() throws InterruptedException {
//        Member memberB = memberRepository.save(MemberFixture.새로운_유저_생성B());
//        DiseaseMember diseaseMemberB = diseaseMemberRepository.save(DiseaseMember.builder()
//                .diseaseInfo(disease).member(memberB).build());
//
//        ExecutorService executor = Executors.newFixedThreadPool(2);
//        CountDownLatch latch = new CountDownLatch(2);
//
//        // 첫 번째 사용자의 수정
//        executor.submit(() -> {
//            try {
//                long startTime = System.currentTimeMillis();
//                DiseaseWikiPatchRequest request = new DiseaseWikiPatchRequest(diseaseWiki.getId(), "First User Content");
//                diseaseWikiService.patchWiki(diseaseMember, request);
//                Thread.sleep(2000); // 첫 번째 사용자가 작업을 완료하기 전까지 일부러 지연
//                long endTime = System.currentTimeMillis();
//                System.out.println("User A start time: " + startTime + " end time: " + endTime);
//            } catch (Exception e) {
//                System.out.println("Exception for User A: " + e + " message: " + e.getMessage());
//            } finally {
//                latch.countDown();
//            }
//        });
//
//        // 두 번째 사용자의 수정
//        executor.submit(() -> {
//            try {
//                Thread.sleep(500); // 첫 번째 사용자가 락을 획득한 후에 두 번째 사용자가 시작하도록 지연
//                long startTime = System.currentTimeMillis();
//                DiseaseWikiPatchRequest request = new DiseaseWikiPatchRequest(diseaseWiki.getId(), "Second User Content");
//                diseaseWikiService.patchWiki(diseaseMemberB, request);
//                long endTime = System.currentTimeMillis();
//                System.out.println("User B start time: " + startTime + " end time: " + endTime);
//            } catch (Exception e) {
//                System.out.println("Exception for User B: " + e + " message: " + e.getMessage());
//            } finally {
//                latch.countDown();
//            }
//        });
//
//        latch.await();  // 두 스레드의 완료를 기다림
//        executor.shutdown();
//
//        // 최종 결과 확인
//        DiseaseWiki result = diseaseWikiRepository.findById(diseaseWiki.getId()).orElseThrow();
//        System.out.println("최종 내용: " + result.getContent());
//    }

}
