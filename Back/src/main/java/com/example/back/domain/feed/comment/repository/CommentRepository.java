package com.example.back.domain.feed.comment.repository;

import com.example.back.domain.feed.comment.Comment;
import com.example.back.domain.feed.feedinfo.FeedInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Optional<List<Comment>> findByFeedInfo(FeedInfo feedInfo);
}
