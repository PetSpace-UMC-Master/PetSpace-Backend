package com.petspace.dev.dto.favorite;

import com.petspace.dev.domain.Favorite;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FavoriteResponseDto {

    private Long id;
    private String roomName;

    public static FavoriteResponseDto of(Favorite favorite) {
        return FavoriteResponseDto.builder()
                .id(favorite.getId())
                .roomName(favorite.getRoom().getRoomName())
                .build();
    }
}
