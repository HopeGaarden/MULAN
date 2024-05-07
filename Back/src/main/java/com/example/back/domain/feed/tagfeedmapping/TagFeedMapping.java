package com.example.back.domain.feed.tagfeedmapping;

import com.example.back.domain.BaseEntity;
import com.example.back.domain.feed.feedinfo.FeedInfo;
import com.example.back.domain.feed.tag.TagInfo;
import jakarta.persistence.*;
import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class TagFeedMapping extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TAG_FEED_MAPPING_ID")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FEED_INFO_ID", nullable = false)
    private FeedInfo feedInfo; // 태그가 달린 피드

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TAG_INFO_ID", nullable = false)
    private TagInfo tagInfo; // 피드에 달린 태크

    @Builder
    public TagFeedMapping (FeedInfo feedInfo, TagInfo tagInfo){
        this.feedInfo = feedInfo;
        this.tagInfo = tagInfo;
    }
}
