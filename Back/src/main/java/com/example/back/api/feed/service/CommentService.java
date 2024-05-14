package com.example.back.api.feed.service;

import com.example.back.api.auth.service.MemberService;
import com.example.back.api.feed.controller.request.CommentRequest;
import com.example.back.api.feed.controller.request.CommentUpdateRequest;
import com.example.back.api.feed.controller.response.CommentResponse;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.feed.comment.Comment;
import com.example.back.domain.feed.comment.repository.CommentRepository;
import com.example.back.domain.feed.feedinfo.FeedInfo;
import com.example.back.domain.feed.feedinfo.repository.FeedInfoRepository;
import com.example.back.global.exception.CommentException;
import com.example.back.global.exception.ExceptionMessage;
import com.example.back.global.exception.FeedInfoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final FeedInfoRepository feedInfoRepository;
    public void createComment(Long memberId, CommentRequest commentRequest) {
        Member member = memberService.memberValidation(memberId);
        FeedInfo feedInfo = feedInfoValidation(commentRequest.feedInfoId());
        Comment comment = new Comment(feedInfo,member, commentRequest.text());
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long commentId, CommentUpdateRequest commentRequest) {
        Comment comment = commentValidation(commentId);
        comment.updateComment(commentRequest.text());
    }

    public void deleteComment(Long commentId) {
        Comment comment = commentValidation(commentId);
        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> getCommentList(Long feedInfoId) {
        FeedInfo feedInfo = feedInfoValidation(feedInfoId);
        List<Comment> comments = getComments(feedInfo);
        return comments.stream()
                .map(CommentResponse::getCommentResponse)
                .collect(Collectors.toList());
    }
    //피드 검증 메서드
    public FeedInfo feedInfoValidation(Long feedInfoId){
        return feedInfoRepository.findById(feedInfoId).orElseThrow(() -> {
            log.error("[Not Found Exception]: {}", ExceptionMessage.FEEDINFO_NOT_FOUND.getText());
            return new FeedInfoException(ExceptionMessage.FEEDINFO_NOT_FOUND);
        });
    }
    // 댓글 목록 반환 메서드
    public List<Comment> getComments(FeedInfo feedInfo){
        return commentRepository.findByFeedInfo(feedInfo).orElse(new ArrayList<>());
    }

    //댓글 검증 메서드
    public Comment commentValidation(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(() -> {
            log.error("[Not Found Exception]: {}", ExceptionMessage.COMMENT_NOT_FOUND.getText());
            return new CommentException(ExceptionMessage.COMMENT_NOT_FOUND);
        });
    }



}
