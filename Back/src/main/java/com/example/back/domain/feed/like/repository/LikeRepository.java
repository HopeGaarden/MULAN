package com.example.back.domain.feed.like.repository;

import com.example.back.domain.auth.member.Member;
import com.example.back.domain.feed.feedinfo.FeedInfo;
import com.example.back.domain.feed.like.LikeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeInfo, Long> {
    Optional<LikeInfo> findByMemberAndFeedInfo(Member member, FeedInfo feedInfo);
    Optional<List<LikeInfo>> findByFeedInfo(FeedInfo feedInfo);
}
