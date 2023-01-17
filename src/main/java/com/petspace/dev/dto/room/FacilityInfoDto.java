package com.petspace.dev.dto.room;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FacilityInfoDto {
    String facilityName;
    String facilityImageUrl;
}
