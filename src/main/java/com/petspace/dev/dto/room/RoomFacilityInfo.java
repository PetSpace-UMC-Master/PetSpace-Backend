package com.petspace.dev.dto.room;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomFacilityInfo {

    private String facilityCategory;
    private String facilityName;
    private String facilityImageUrl;

}
