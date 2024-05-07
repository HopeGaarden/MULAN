package com.example.back.api.feed.service;

import com.example.back.api.auth.service.MemberService;
import com.example.back.api.feed.controller.response.FeedInfoResponse;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.auth.member.repository.MemberRepository;
import com.example.back.domain.feed.bookmark.Bookmark;
import com.example.back.domain.feed.bookmark.repository.BookmarkRepository;
import com.example.back.domain.feed.feedinfo.FeedInfo;
import com.example.back.domain.feed.feedinfo.repository.FeedInfoRepository;
import com.example.back.global.exception.BookmarkException;
import com.example.back.global.exception.ExceptionMessage;
import com.example.back.global.exception.FeedInfoException;
import com.example.back.global.exception.MemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final MemberService memberService;
    private final FeedInfoService feedInfoService;

    public void createBookmark(Long memberId, Long feedInfoId) {
        Member member = memberService.memberValidation(memberId);
        FeedInfo feedInfo = feedInfoService.feedInfoValidation(feedInfoId);
        Bookmark bookmark = new Bookmark(feedInfo,member);
        bookmarkRepository.save(bookmark);
    }

    public void deleteBookmark(Long memberId, Long feedInfoId) {
        Member member = memberService.memberValidation(memberId);
        FeedInfo feedInfo = feedInfoService.feedInfoValidation(feedInfoId);
        Bookmark bookmark = bookmarkValidation(member,feedInfo);
        bookmarkRepository.delete(bookmark);
    }

    // 북마크 검증 메서드
    public Bookmark bookmarkValidation(Member member, FeedInfo feedInfo){
        return bookmarkRepository.findByMemberAndFeedInfo(member, feedInfo).orElseThrow(() -> {
            log.error("[Not Found Exception]: {}", ExceptionMessage.BOOKMARK_NOT_FOUND.getText());
            return new BookmarkException(ExceptionMessage.BOOKMARK_NOT_FOUND);
        });
    }

    // 북마크 존재 여부 확인 메서드
    public Boolean isBookmarkPresent(Member member, FeedInfo feedInfo) {
        return bookmarkRepository.findByMemberAndFeedInfo(member, feedInfo).isPresent();
    }

}
