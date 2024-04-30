package com.example.back.domain.feed.tag;

import com.example.back.domain.BaseEntity;
import com.example.back.domain.feed.feedInfo.FeedInfo;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TagInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_INFO_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEED_INFO_ID", nullable = false)
    private FeedInfo feedInfo; // 태그가 달린 피드

    @Column(name = "TAG_TEXT", nullable = false)
    private String tagText; // 해시태그 내용

    @Builder
    public TagInfo(FeedInfo feedInfo, String tagText) {
        this.feedInfo = feedInfo;
        this.tagText = tagText;
    }
}
