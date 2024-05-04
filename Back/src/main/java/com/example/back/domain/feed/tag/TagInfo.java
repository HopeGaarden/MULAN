package com.example.back.domain.feed.tag;

import com.example.back.domain.BaseEntity;
import com.example.back.domain.feed.tagfeedmapping.TagFeedMapping;
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

    @Column(name = "TAG_NAME", nullable = false)
    private String tagName; // 해시태그 내용

    @Builder
    public TagInfo( String tagName) {
        this.tagName = tagName;
    }

}
