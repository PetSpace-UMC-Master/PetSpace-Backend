package com.petspace.dev.dto.room;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.petspace.dev.domain.Facility;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoomFacilityInfo {

    @JsonIgnore
    private String facilityCategory;

    private String facilityName;
    private String facilityImageUrl;

    protected static RoomFacilityInfo of(Facility facility) {
        RoomFacilityInfo roomFacilityInfo = RoomFacilityInfo.builder()
                .facilityCategory(facility.getCategory())
                .facilityName(facility.getFacilityName())
                .facilityImageUrl(facility.getFacilityImageUrl())
                .build();
        return roomFacilityInfo;
    }
}

