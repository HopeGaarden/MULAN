package com.example.back.domain.feed.like;

import com.example.back.domain.BaseEntity;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.feed.feedinfo.FeedInfo;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Like extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LIKE_INFO_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEED_INFO_ID", nullable = false)
    private FeedInfo feedInfo;          // 좋아요가 달린 피드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;              // 좋아요를 누른 사용자

    @Builder
    public Like(FeedInfo feedInfo, Member member) {
        this.feedInfo = feedInfo;
        this.member = member;
    }
}
