package com.example.back.api.wiki.controller;

import com.example.back.api.disease.service.DiseaseMemberService;
import com.example.back.api.wiki.controller.request.DiseaseWikiPatchRequest;
import com.example.back.api.wiki.controller.request.DiseaseWikiSaveRequest;
import com.example.back.api.wiki.service.DiseaseWikiService;
import com.example.back.common.response.JsonResult;
import com.example.back.domain.auth.disease.DiseaseMember;
import com.example.back.domain.auth.member.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/wiki")
@RequiredArgsConstructor
public class DiseaseWikiController {
    private final DiseaseWikiService diseaseWikiService;
    private final DiseaseMemberService diseaseMemberService;

    // 위키 백과 리스트 조회
    @GetMapping
    public JsonResult<?> findAllWiki() {

        return JsonResult.successOf(diseaseWikiService.findAllWikis());
    }

    // 위키 백과 상세 조회
    @GetMapping("/{wikiId}")
    public JsonResult<?> findWiki(@PathVariable(name = "wikiId") Long wikiId) {

        return JsonResult.successOf(diseaseWikiService.findWiki(wikiId));
    }

    // 위키 백과 새로 등록
    @PostMapping
    public JsonResult<?> saveWiki(@AuthenticationPrincipal Member member,
                                  @Valid @RequestBody DiseaseWikiSaveRequest request) {
        // 위키를 생성할 수 있는 앱의 사용자인지 권한 검증
        DiseaseMember diseaseMember = diseaseMemberService.isDiseaseMember(member);

        diseaseWikiService.saveWiki(diseaseMember, request.content());

        return JsonResult.successOf("위키 등록에 성공하였습니다.");
    }


//     위키 백과 수정
    @PatchMapping("/{wikiId}")
    public JsonResult<?> findWiki(@AuthenticationPrincipal Member member,
                                  @PathVariable(name = "wikiId") Long wikiId,
                                  @Valid @RequestBody DiseaseWikiPatchRequest request) {
        // 해당 위키를 수정할 수 있는 사용자인지 권한 검증
        DiseaseMember diseaseMember = diseaseMemberService.isSpecificDiseaseMember(member, request.DiseaseWikiId());

        diseaseWikiService.patchWiki(wikiId, diseaseMember, request);

        return JsonResult.successOf("위키 수정에 성공하였습니다.");
    }
}
