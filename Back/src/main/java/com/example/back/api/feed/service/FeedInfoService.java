package com.example.back.api.feed.service;

import com.example.back.api.auth.service.MemberService;
import com.example.back.api.feed.controller.request.FeedInfoRequest;
import com.example.back.api.feed.controller.request.FeedInfoSliceRequest;
import com.example.back.api.feed.controller.request.FeedInfoUpdateRequest;
import com.example.back.api.feed.controller.response.FeedInfoListResponse;
import com.example.back.api.feed.controller.response.FeedInfoResponse;
import com.example.back.common.utils.imagefile.FileUtils;
import com.example.back.common.utils.imagefile.ResultFileStore;
import com.example.back.domain.auth.member.Member;
import com.example.back.domain.feed.feedinfo.FeedInfo;
import com.example.back.domain.feed.feedinfo.repository.FeedInfoRepository;
import com.example.back.domain.feed.tag.TagInfo;
import com.example.back.domain.feed.tagfeedmapping.TagFeedMapping;
import com.example.back.domain.feed.tagfeedmapping.repository.TagFeedMappingRespoistory;
import com.example.back.global.exception.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FeedInfoService {
    private final FileUtils fileStore;
    private final FeedInfoRepository feedInfoRepository;
    private final TagFeedMappingRespoistory tagFeedMappingRespoistory;
    private final TagInfoService tagInfoService;
    private final MemberService memberService;
    private final BookmarkService bookmarkService;
    private final CommentService commentService;
    private final LikeService likeService;

    @Transactional
    public void createFeedInfo(Long memberId, FeedInfoRequest feedInfoRequest, List<MultipartFile> images){
        //피드 저장
        String resultFileStore = getResultFileStore(images);
        Member member = memberService.memberValidation(memberId);
        FeedInfo feedInfo =
                new FeedInfo(member,
                             feedInfoRequest.category(),
                             feedInfoRequest.content(),
                             resultFileStore,
                             feedInfoRequest.status()
                             );
        feedInfoRepository.save(feedInfo);
        //해시태그 저장
        String[] tagNames = feedInfoRequest.tags();
        for (String tagName : tagNames) {
            // 태그 엔티티 조회 혹은 생성
            TagInfo tagInfo = tagInfoService.getOrCreateTagInfo(tagName);
            // 매핑 테이블에 데이터 삽입
            TagFeedMapping tagFeedMapping = new TagFeedMapping(feedInfo, tagInfo);
            tagFeedMappingRespoistory.save(tagFeedMapping);
        }
    }

    @Transactional(readOnly = true)
    public Slice<FeedInfoListResponse> getFeedInfoListSliceSearch(Long memberId,FeedInfoSliceRequest feedInfoSliceRequest){
        Member member = memberService.memberValidation(memberId);
        Pageable pageable = PageRequest.of(feedInfoSliceRequest.getPage(), feedInfoSliceRequest.getSize());
        Slice<FeedInfo> feedInfoSlice = feedInfoRepository
                .findByContentOrTagNameOrderByIdDesc(feedInfoSliceRequest.getSearchKeyword(), pageable);
        List<FeedInfoListResponse> feedInfoListResponse = feedInfoSlice.getContent().stream()
                .map(feedInfo -> {
                    Integer commentCount = commentService.getComments(feedInfo).size();
                    Integer likeCount = likeService.getLikes(feedInfo).size();
                    Boolean like = likeService.pressLike(member, feedInfo);
                    Boolean bookmark = bookmarkService.isBookmarkPresent(member,feedInfo);
                    return FeedInfoListResponse
                            .ResponseGetFeedInfoList(feedInfo, commentCount, likeCount, like, bookmark);
                })
                .toList();
        return new SliceImpl<>(feedInfoListResponse, pageable, feedInfoSlice.hasNext());

    }

    @Transactional(readOnly = true)
    public FeedInfoResponse getFeedInfo(Long feedInfoId,Long memberId) {
        FeedInfo feedInfo = feedInfoValidation(feedInfoId);
        Member member = memberService.memberValidation(memberId);
        return FeedInfoResponse.ResponseGetFeedInfo(feedInfo,
                commentService.getComments(feedInfo).size(),
                likeService.getLikes(feedInfo).size(),
                likeService.pressLike(member, feedInfo),
                bookmarkService.isBookmarkPresent(member,feedInfo));
    }

    @Transactional
    public void updateFeedInfo(Long feedInfoId, FeedInfoUpdateRequest feedInfoUpdateRequest, List<MultipartFile> images){
        //피드 수정
        String resultFileStore = getResultFileStore(images);
        FeedInfo feedInfo = feedInfoValidation(feedInfoId);
        feedInfo.updateFeedInfo(feedInfoUpdateRequest.content(), resultFileStore,feedInfoUpdateRequest.status());
        // 태그-피드 매핑 엔티티 업데이트
        String[] tagNames = feedInfoUpdateRequest.tags();
        List<TagFeedMapping> tagFeedMappings = new ArrayList<>();
        for (String tagName : tagNames) {
            // 태그 엔티티 조회 혹은 생성
            TagInfo tagInfo = tagInfoService.getOrCreateTagInfo(tagName);
            // 태그-피드 매핑 엔티티 생성
            TagFeedMapping tagFeedMapping = TagFeedMapping.builder()
                    .feedInfo(feedInfo)
                    .tagInfo(tagInfo)
                    .build();
            tagFeedMappings.add(tagFeedMapping);
        }
        // 기존에 연결된 태그-피드 매핑 엔티티 삭제 후 새로운 매핑 엔티티 추가
        tagFeedMappingRespoistory.deleteAllByFeedInfo(feedInfo);
        tagFeedMappingRespoistory.saveAll(tagFeedMappings);
    }
    @Transactional
    public void deleteFeedInfo(Long feedInfoId) {
        FeedInfo feedInfo = feedInfoValidation(feedInfoId);
        tagFeedMappingRespoistory.deleteAllByFeedInfo(feedInfo);
        feedInfoRepository.delete(feedInfo);
    }
    //피드 검증 메서드
    public FeedInfo feedInfoValidation(Long feedInfoId){
        return feedInfoRepository.findById(feedInfoId).orElseThrow(() -> {
            log.error("[Not Found Exception]: {}", ExceptionMessage.FEEDINFO_NOT_FOUND.getText());
            return new FeedInfoException(ExceptionMessage.FEEDINFO_NOT_FOUND);
        });
    }
    // 파일 저장 메서드
    private String getResultFileStore(List<MultipartFile> images) {
        List<ResultFileStore> resultFileStore = null;
        try {
            resultFileStore = fileStore.storeFiles(images);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resultFileStore.stream()
                .map(rfs -> rfs.folderPath() + "/" + rfs.storeFileName()) // 각 요소를 문자열로 변환
                .collect(Collectors.joining(","));
    }
}
