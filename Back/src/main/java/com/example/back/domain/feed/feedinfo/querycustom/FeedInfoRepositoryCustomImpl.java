package com.example.back.domain.feed.feedinfo.querycustom;


import com.example.back.domain.feed.feedinfo.FeedInfo;
import com.example.back.domain.feed.feedinfo.QFeedInfo;
import com.example.back.domain.feed.tag.QTagInfo;
import com.example.back.domain.feed.tagfeedmapping.QTagFeedMapping;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import java.util.List;

@RequiredArgsConstructor
public class FeedInfoRepositoryCustomImpl implements FeedInfoRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;
    QFeedInfo feedInfo = QFeedInfo.feedInfo;
    QTagInfo tagInfo = QTagInfo.tagInfo;
    QTagFeedMapping tagFeedMapping = QTagFeedMapping.tagFeedMapping;
    @Override
    public Slice<FeedInfo> findByContentOrTagNameOrderByIdDesc(String keyword, Pageable pageable) {
        List<FeedInfo> entity = jpaQueryFactory
                .select(feedInfo)
                .from(feedInfo)
                .leftJoin(tagFeedMapping).on(tagFeedMapping.feedInfo.id.eq(feedInfo.id))
                .leftJoin(tagFeedMapping.tagInfo).on(tagFeedMapping.tagInfo.id.eq(tagInfo.id))
                .where(feedInfo.content.like("%" + keyword + "%")
                        .or(tagInfo.tagName.like("%" + keyword + "%")))
                .orderBy(feedInfo.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = jpaQueryFactory
                .select(feedInfo.count())
                .from(feedInfo)
                .leftJoin(tagFeedMapping).on(tagFeedMapping.feedInfo.id.eq(feedInfo.id))
                .leftJoin(tagFeedMapping.tagInfo).on(tagFeedMapping.tagInfo.id.eq(tagInfo.id))
                .where(feedInfo.content.like("%" + keyword + "%")
                        .or(tagInfo.tagName.like("%" + keyword + "%")))
                .fetchFirst();

        return new SliceImpl<>(entity, pageable, total > pageable.getOffset() + entity.size());
    }
}
