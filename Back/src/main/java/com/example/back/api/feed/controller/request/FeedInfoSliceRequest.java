package com.example.back.api.feed.controller.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FeedInfoSliceRequest {
    String searchKeyword;
    @NotNull(message = "유효하지 않은 값입니다.")
    @Min(value = 0, message = "음수일 수 없습니다.")
    private Integer page;
    @NotNull(message = "유효하지 않은 값입니다.")
    private Integer size;

    @Builder
    public FeedInfoSliceRequest(Integer page, Integer size) {
        this.page = page;
        this.size = size;
    }


}
