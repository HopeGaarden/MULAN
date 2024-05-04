package com.example.back.domain.feed.bookmark.repository;

import com.example.back.domain.auth.member.Member;
import com.example.back.domain.feed.bookmark.Bookmark;
import com.example.back.domain.feed.comment.Comment;
import com.example.back.domain.feed.feedinfo.FeedInfo;
import com.example.back.domain.feed.like.LikeInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {
    Optional<Bookmark> findByMemberAndFeedInfo(Member member, FeedInfo feedInfo);
}
