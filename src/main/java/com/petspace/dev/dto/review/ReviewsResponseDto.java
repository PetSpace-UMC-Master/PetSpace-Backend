package com.petspace.dev.dto.review;


import com.petspace.dev.domain.Review;
import com.petspace.dev.domain.image.ReviewImage;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReviewsResponseDto {
    private Long id;
    private String nickName;
    private List<ReviewImage> reviewImage;
    private int score;
    private String content;

    public static ReviewsResponseDto of(Review review) {
        return ReviewsResponseDto.builder()
                .id(review.getId())
                .nickName(review.getReservation().getUser().getNickname())
                .reviewImage(review.getReviewImages())
                .score(review.getScore())
                .content(review.getContent())
                .build();
    }
}
