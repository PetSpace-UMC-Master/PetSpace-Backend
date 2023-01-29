package com.petspace.dev.dto.room;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomDetailReview {

    // TODO user image url 추가
    private Long userId;
    private String nickname;
    private int score;
    private String createdAt;
    private String description;

}
