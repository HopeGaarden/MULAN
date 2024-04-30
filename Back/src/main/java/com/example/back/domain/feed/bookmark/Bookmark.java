package com.example.back.domain.feed.bookmark;

import com.example.back.domain.BaseEntity;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.feed.feedInfo.FeedInfo;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Bookmark extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "BOOKMARK_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEED_INFO_ID", nullable = false)
    private FeedInfo feedInfo;          // 북마크가 눌린 피드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member member;              // 북마크를 누른 사용자

    @Builder
    public Bookmark(FeedInfo feedInfo, Member member) {
        this.feedInfo = feedInfo;
        this.member = member;
    }
}
