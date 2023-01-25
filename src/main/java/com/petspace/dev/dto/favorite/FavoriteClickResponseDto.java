package com.petspace.dev.dto.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavoriteClickResponseDto {

    private Long id;
    private String roomName;

    @JsonProperty("isClicked")
    private Boolean isClicked;
}
