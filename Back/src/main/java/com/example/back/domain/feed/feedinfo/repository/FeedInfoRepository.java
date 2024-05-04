package com.example.back.domain.feed.feedinfo.repository;

import com.example.back.domain.auth.member.Member;
import com.example.back.domain.feed.feedinfo.FeedInfo;
import com.example.back.domain.feed.feedinfo.querycustom.FeedInfoRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedInfoRepository extends JpaRepository<FeedInfo, Long>, FeedInfoRepositoryCustom {
}
