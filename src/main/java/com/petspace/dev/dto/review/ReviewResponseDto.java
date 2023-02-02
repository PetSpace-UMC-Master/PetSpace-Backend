package com.petspace.dev.dto.review;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReviewResponseDto {

    private Long id;
    private Integer score;
    private String content;
}
