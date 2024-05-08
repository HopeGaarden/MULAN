package com.example.back.domain.feed.like.repository;

import com.example.back.domain.auth.member.Member;
import com.example.back.domain.feed.feedinfo.FeedInfo;
import com.example.back.domain.feed.like.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
    Optional<Like> findByMemberAndFeedInfo(Member member, FeedInfo feedInfo);
    Optional<List<Like>> findByFeedInfo(FeedInfo feedInfo);
}
