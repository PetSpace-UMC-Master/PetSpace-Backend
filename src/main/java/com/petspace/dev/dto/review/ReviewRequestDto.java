package com.petspace.dev.dto.review;

import com.petspace.dev.domain.*;
import com.petspace.dev.domain.image.ReviewImage;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class ReviewRequestDto {

    private Reservation reservation;
    private List<ReviewImage> reviewImages = new ArrayList<>();
    private int score;
    private String content;
    private Status status;


    public Review toEntity() {
        return Review.builder()
                .reviewImages(reviewImages)
                .reservation(reservation)
                .score(score)
                .content(content)
                .status(Status.ACTIVE)
                .build();
    }

}
