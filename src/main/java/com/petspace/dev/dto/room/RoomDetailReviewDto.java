package com.petspace.dev.dto.room;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class RoomDetailReviewDto {
    private String nickname;
    private LocalDateTime createdAt;
    private String description;
}
