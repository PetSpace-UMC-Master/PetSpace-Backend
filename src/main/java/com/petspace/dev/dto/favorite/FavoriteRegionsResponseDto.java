package com.petspace.dev.dto.favorite;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FavoriteRegionsResponseDto {

    private Long id;
    private List<String> regions;
}
