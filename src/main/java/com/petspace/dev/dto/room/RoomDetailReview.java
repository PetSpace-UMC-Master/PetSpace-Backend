package com.petspace.dev.dto.room;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomDetailReview {

    private Long userId;
    private String profileImage;
    private String nickname;
    private int score;
    private String createdAt;
    private String description;

}
