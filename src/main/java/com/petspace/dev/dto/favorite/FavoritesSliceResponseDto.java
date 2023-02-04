package com.petspace.dev.dto.favorite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FavoritesSliceResponseDto {
    List<FavoritesResponseDto> favorites;

    private long page;
    @JsonProperty(value = "isLast")
    private Boolean isLast;
}
