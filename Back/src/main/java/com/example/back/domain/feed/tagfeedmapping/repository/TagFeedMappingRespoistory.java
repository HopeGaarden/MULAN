package com.example.back.domain.feed.tagfeedmapping.repository;

import com.example.back.domain.feed.feedinfo.FeedInfo;
import com.example.back.domain.feed.tagfeedmapping.TagFeedMapping;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagFeedMappingRespoistory extends JpaRepository<TagFeedMapping, Long> {
    void deleteByFeedInfo(FeedInfo feedInfo);
}
