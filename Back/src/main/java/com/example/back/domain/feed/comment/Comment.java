package com.example.back.domain.feed.comment;

import com.example.back.domain.BaseEntity;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.feed.feedinfo.FeedInfo;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COMMENT_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEED_INFO_ID", nullable = false)
    private FeedInfo feedInfo;              // 댓글이 달린 피드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;                  // 댓글 작성자

    @Column(name = "TEXT", nullable = false)
    private String text;                    // 댓글 내용

    @Builder
    public Comment(FeedInfo feedInfo, Member member, String text) {
        this.feedInfo = feedInfo;
        this.member = member;
        this.text = text;
    }
}
