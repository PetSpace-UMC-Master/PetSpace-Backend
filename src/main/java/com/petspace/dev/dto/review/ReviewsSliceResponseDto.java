package com.petspace.dev.dto.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewsSliceResponseDto {
    List<ReviewsResponseDto> reviews;

    private double averageReviewScore;
    private int numberOfReview;

    private long page;
    @JsonProperty(value = "isLast")
    private Boolean isLast;
}
