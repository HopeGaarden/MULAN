package com.example.back.api.feed.controller;

import com.example.back.api.feed.controller.request.FeedInfoRequest;
import com.example.back.api.feed.controller.request.FeedInfoSliceRequest;
import com.example.back.api.feed.controller.request.FeedInfoUpdateRequest;
import com.example.back.api.feed.controller.response.FeedInfoListResponse;
import com.example.back.api.feed.controller.response.FeedInfoResponse;
import com.example.back.api.feed.service.FeedInfoService;
import com.example.back.domain.auth.member.Member;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/feedInfo")
@RequiredArgsConstructor
public class FeedInfoController {
    private final FeedInfoService feedInfoService;

    @PostMapping()
    public ResponseEntity<String> createFeedInfo(@AuthenticationPrincipal Member member,
                                                 @RequestPart @Valid FeedInfoRequest feedInfo,
                                                 @RequestPart List<MultipartFile> images){
        feedInfoService.createFeedInfo(member.getId(),feedInfo,images);
        return ResponseEntity.ok("피드를 생성 하였습니다.");
    }

    @GetMapping
    public ResponseEntity<Slice<FeedInfoListResponse>> getFeedInfoList(@AuthenticationPrincipal Member member
                                                                  ,@RequestBody FeedInfoSliceRequest feedInfoSliceRequest) {
        Slice<FeedInfoListResponse> responseGetPostList = feedInfoService.getFeedInfoListSliceSearch(member.getId(),feedInfoSliceRequest);
        return ResponseEntity.ok()
                .body(responseGetPostList);
    }

    @GetMapping("/{feedInfoId}")
    public ResponseEntity<FeedInfoResponse> getFeedInfo(@AuthenticationPrincipal Member member
                                                        ,@PathVariable Long feedInfoId) {
        FeedInfoResponse feedInfoResponse = feedInfoService.getFeedInfo(feedInfoId,member.getId());
        return ResponseEntity.ok()
                .body(feedInfoResponse);
    }

    @PatchMapping("/{feedInfoId}")
    public ResponseEntity<String> updateFeedInfo(@PathVariable Long feedInfoId,
                                                 @RequestPart @Valid FeedInfoUpdateRequest feedInfoUpdateRequest,
                                                 @RequestPart List<MultipartFile> images) {
        feedInfoService.updateFeedInfo(feedInfoId, feedInfoUpdateRequest,images);
        return ResponseEntity.ok("피드를 수정 하였습니다.");
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deleteFeedInfo(@PathVariable Long feedInfoId) {
        feedInfoService.deleteFeedInfo(feedInfoId);
        return ResponseEntity.ok("피드를 삭제 하였습니다.");
    }
}
