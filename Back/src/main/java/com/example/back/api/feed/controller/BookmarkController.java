package com.example.back.api.feed.controller;

import com.example.back.api.feed.service.BookmarkService;
import com.example.back.domain.auth.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookmark")
@RequiredArgsConstructor
public class BookmarkController {
    private final BookmarkService bookmarkService;

    // 게시물 저장
    @PostMapping("{feedInfoId}")
    public ResponseEntity<String> createBookmark(@AuthenticationPrincipal Member member,
                                                 @PathVariable Long feedInfoId) {
        bookmarkService.createBookmark(member.getId(),feedInfoId);
        return ResponseEntity.ok("해당 게시물을 저장 하였습니다.");
    }

    // 게시물 저장 취소
    @DeleteMapping("/{feedInfoId}")
    public ResponseEntity<String> deleteBookmark(@AuthenticationPrincipal Member member,
                                                 @PathVariable Long feedInfoId) {
        bookmarkService.deleteBookmark(member.getId(),feedInfoId);
        return ResponseEntity.ok("해당 게시물의 저장을 취소 하였습니다.");
    }
}
