package com.petspace.dev.dto.room;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomDetailFacilityDto {
    private String facilityName;
    private String facilityImageUrl;
}
