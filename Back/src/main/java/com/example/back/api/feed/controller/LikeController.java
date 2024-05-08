package com.example.back.api.feed.controller;

import com.example.back.api.feed.controller.response.LikeMemberResponse;
import com.example.back.api.feed.service.LikeService;
import com.example.back.domain.auth.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/like")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    // 좋아요
    @PostMapping("{feedInfoId}")
    public ResponseEntity<String> createLike(@AuthenticationPrincipal Member member,
                                             @PathVariable Long feedInfoId) {
        likeService.createLike(member.getId(),feedInfoId);
        return ResponseEntity.ok("해당 게시물에 공감 하였습니다.");
    }

    // 좋아요 취소
    @DeleteMapping("/{feedInfoId}")
    public ResponseEntity<String> deleteLike(@AuthenticationPrincipal Member member,
                                             @PathVariable Long feedInfoId) {
        likeService.deleteLike(member.getId(),feedInfoId);
        return ResponseEntity.ok("해당 게시물의 공감을 취소 하였습니다.");
    }

    // 공감한 사람 목록 조회
    @GetMapping("{feedInfoId}")
    public ResponseEntity<List<LikeMemberResponse>> getLikeMemberList(@PathVariable Long feedInfoId){
        List<LikeMemberResponse> likeMemberList = likeService.getLikeMemberList(feedInfoId);
        return ResponseEntity.ok()
                .body(likeMemberList);
    }
}
