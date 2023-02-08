package com.petspace.dev.dto.review;

import com.petspace.dev.domain.Review;
import com.petspace.dev.domain.image.ReviewImage;
import com.petspace.dev.util.formatter.DayAfterFormatter;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Builder
public class ReviewsResponseDto {
    private Long id;
    private String nickName;
    private String profileImage;
    private List<String> reviewImage;
    private int score;
    private String dayAfterCreated;
    private String content;

    public static ReviewsResponseDto of(Review review) {

        List<String> reviewImageUrls = review.getReviewImages().stream()
                .map(ReviewImage::getReviewImageUrl)
                .collect(Collectors.toList());

        return ReviewsResponseDto.builder()
                .id(review.getId())
                .nickName(review.getUser().getNickname())
                .profileImage(review.getUser().getProfileImage())
                .reviewImage(reviewImageUrls)
                .score(review.getScore())
                .dayAfterCreated(DayAfterFormatter.formattingDayAfter(review.getCreatedAt()))
                .content(review.getContent())
                .build();
    }
}
