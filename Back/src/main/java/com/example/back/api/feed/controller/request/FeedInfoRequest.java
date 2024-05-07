package com.example.back.api.feed.controller.request;

import com.example.back.domain.feed.feedinfo.FeedCategory;
import com.example.back.domain.feed.feedinfo.FeedStatus;
import com.example.back.domain.feed.tag.TagInfo;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record FeedInfoRequest(    FeedCategory category,
                                  String content,
                                  FeedStatus status,
                                  String[] tags
) {
}
