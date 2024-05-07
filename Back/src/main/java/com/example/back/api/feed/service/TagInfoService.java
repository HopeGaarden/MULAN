package com.example.back.api.feed.service;

import com.example.back.domain.feed.tag.TagInfo;
import com.example.back.domain.feed.tag.repository.TagInfoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TagInfoService {
    private final TagInfoRepository tagInfoRepository;

    public TagInfo getOrCreateTagInfo (String tagName){
        return tagInfoRepository.findByTagName(tagName)
                .orElseGet(() -> tagInfoRepository.save(new TagInfo(tagName)));
    }
}
