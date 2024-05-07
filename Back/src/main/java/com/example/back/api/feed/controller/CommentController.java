package com.example.back.api.feed.controller;

import com.example.back.api.feed.controller.request.CommentRequest;
import com.example.back.api.feed.controller.request.CommentUpdateRequest;
import com.example.back.api.feed.controller.response.CommentResponse;
import com.example.back.api.feed.service.CommentService;
import com.example.back.domain.auth.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<String> createComment(@AuthenticationPrincipal Member member,
                                                @RequestBody CommentRequest commentRequest) {
        commentService.createComment(member.getId(),commentRequest);
        return ResponseEntity.ok("댓글을 생성 하였습니다.");
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<String> updateComment(@PathVariable Long commentId,
                                                @RequestBody CommentUpdateRequest commentUpdateRequest) {
        commentService.updateComment(commentId,commentUpdateRequest);
        return ResponseEntity.ok()
                .body(commentUpdateRequest.text());
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<String> deleteComment(@RequestBody Long commentId){
        commentService.deleteComment(commentId);
        return ResponseEntity.ok("댓글을 삭제 하였습니다.");
    }

    @GetMapping("{feedInfoId}")
    public ResponseEntity<List<CommentResponse>> getCommentList(@PathVariable Long feedInfoId) {
        List<CommentResponse> commentResponses = commentService.getCommentList(feedInfoId);
        return ResponseEntity.ok()
                .body(commentResponses);
    }

}
