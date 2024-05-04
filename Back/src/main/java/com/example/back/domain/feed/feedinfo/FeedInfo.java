package com.example.back.domain.feed.feedinfo;

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
    private Member member;                    // 작성자

    @Enumerated(EnumType.STRING)
    @Column(name = "FEED_CATEGORY", nullable = false)
    private FeedCategory category;              // 피드 카테고리

    @Column(name = "CONTENT", nullable = false)
    private String content;                     // 피드 내용

    @Column(name = "IMAGES")
    private String images;                      // 이미지 URL 리스트

    @Enumerated(EnumType.STRING)
    @Column(name = "FEED_STATUS", nullable = false)
    @ColumnDefault(value = "'PUBLIC'")
    private FeedStatus status;                  // 피드 상태

    @Builder
    public FeedInfo(Member member, FeedCategory category, String content, String images, FeedStatus status) {
        this.member = member;
        this.category = category;
        this.content = content;
        this.images = images;
        this.status = status;
    }
}

