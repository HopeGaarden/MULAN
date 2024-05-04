package com.example.back.domain.feed.tag.repository;

import com.example.back.domain.feed.feedinfo.FeedInfo;
import com.example.back.domain.feed.tag.TagInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagInfoRepository extends JpaRepository<TagInfo, Long> {
    Optional<TagInfo> findByTagName(String tagName);
}
