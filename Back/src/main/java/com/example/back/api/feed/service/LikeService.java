package com.example.back.api.feed.service;

import com.example.back.api.auth.service.MemberService;
import com.example.back.api.feed.controller.response.LikeMemberResponse;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.feed.feedinfo.FeedInfo;
import com.example.back.domain.feed.like.Like;
import com.example.back.domain.feed.like.repository.LikeRepository;
import com.example.back.global.exception.ExceptionMessage;
import com.example.back.global.exception.LikeException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final MemberService memberService;
    private final FeedInfoService feedInfoService;

    public void createLike(Long memberId, Long feedInfoId) {
        Member member = memberService.memberValidation(memberId);
        FeedInfo feedInfo = feedInfoService.feedInfoValidation(feedInfoId);
        Like like = new Like(feedInfo,member);
        likeRepository.save(like);
    }

    public void deleteLike(Long memberId, Long feedInfoId) {
        Member member = memberService.memberValidation(memberId);
        FeedInfo feedInfo = feedInfoService.feedInfoValidation(feedInfoId);
        Like like = likeValidation(member,feedInfo);
        likeRepository.delete(like);
    }

    public List<LikeMemberResponse> getLikeMemberList(Long feedInfoId) {
        FeedInfo feedInfo = feedInfoService.feedInfoValidation(feedInfoId);
        List<Like> likes = getLikes(feedInfo);
        return likes.stream()
                .map(LikeMemberResponse::getLikesResponse)
                .collect(Collectors.toList());
    }

    // 좋아요 검증 메서드
    public Like likeValidation(Member member, FeedInfo feedInfo){
        return likeRepository.findByMemberAndFeedInfo(member, feedInfo).orElseThrow(() -> {
            log.error("[Not Found Exception]: {}", ExceptionMessage.LIKE_NOT_FOUND.getText());
            return new LikeException(ExceptionMessage.LIKE_NOT_FOUND);
        });
    }

    // 좋아요 목록 반환 메서드
    public List<Like> getLikes(FeedInfo feedInfo){
        return likeRepository.findByFeedInfo(feedInfo).orElse(new ArrayList<>());
    }

    // 좋아요 여부 반환 메서드
    public Boolean pressLike(Member member, FeedInfo feedInfo){
        return likeRepository.findByMemberAndFeedInfo(member,feedInfo).isPresent();
    }
}
