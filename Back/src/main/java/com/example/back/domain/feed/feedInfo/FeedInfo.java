package com.example.back.domain.feed.feedInfo;

import com.example.back.domain.BaseEntity;
import com.example.back.domain.auth.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

@Getter
@DynamicInsert
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class FeedInfo extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "FEED_INFO_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID", nullable = false)
    private Member follower;                    // 팔로워

    @Enumerated(EnumType.STRING)
    @Column(name = "FEED_CATEGORY", nullable = false)
    private FeedCategory category;              // 피드 카테고리

    @Column(name = "CONTENT", nullable = false)
    private String content;                     // 피드 내용

    @Column(name = "IMAGES")
    private String images;                      // 이미지 URL 리스트

    @Column(name = "VIEW_CNT")
    private int viewCnt = 0;                    // 조회 수

    @Enumerated(EnumType.STRING)
    @Column(name = "FEED_STATUS", nullable = false)
    @ColumnDefault(value = "'PUBLIC'")
    private FeedStatus status;                  // 피드 상태

    @Builder
    public FeedInfo(Member follower, FeedCategory category, String content, String images, int viewCnt, FeedStatus status) {
        this.follower = follower;
        this.category = category;
        this.content = content;
        this.images = images;
        this.viewCnt = viewCnt;
        this.status = status;
    }
}

