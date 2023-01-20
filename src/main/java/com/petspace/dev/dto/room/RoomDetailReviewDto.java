package com.petspace.dev.dto.room;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@Builder
public class RoomDetailReviewDto {

    private Long userId;
    private String nickname;
    private LocalDateTime createdAt;
    private String description;

}
