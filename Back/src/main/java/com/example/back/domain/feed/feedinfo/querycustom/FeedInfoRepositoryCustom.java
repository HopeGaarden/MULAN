package com.example.back.domain.feed.feedinfo.querycustom;

import com.example.back.domain.feed.feedinfo.FeedInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface FeedInfoRepositoryCustom {
    Slice<FeedInfo> findByContentOrTagNameOrderByIdDesc(String content, Pageable pageable);
}
