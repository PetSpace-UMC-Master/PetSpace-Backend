package com.petspace.dev.dto.room;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomDetailFacility {

    private String facilityName;
    private String facilityImageUrl;

}
