package com.petspace.dev.dto.review;


import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.image.ReviewImage;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ReviewListResponseDto {
    private Long id;
    private String nickName;
    private List<ReviewImage> reviewImage;
    private float score;
    private String content;
    private LocalDateTime createdAt;
    private Status status;

}
