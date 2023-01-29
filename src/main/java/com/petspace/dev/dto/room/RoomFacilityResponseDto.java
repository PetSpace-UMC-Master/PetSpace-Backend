package com.petspace.dev.dto.room;

import com.petspace.dev.domain.Room;
import com.petspace.dev.domain.RoomFacility;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class RoomFacilityResponseDto {

    @Getter @Setter
    private class FacilitiesWithSameCategory{
        private String category;
        private List<RoomFacilityInfo> facilities;
    }

    private List<FacilitiesWithSameCategory> allFacilityInfos;

    public RoomFacilityResponseDto(Room room){
        this.allFacilityInfos = getAllFacilities(room);
    }

    private List<FacilitiesWithSameCategory> getAllFacilities(Room room){

        // 모든 Facility 정보 가져오기 -> Category 그룹화 이전
        List<RoomFacilityInfo> facilities = room.getRoomFacilities()
                .stream().map(RoomFacility::getFacility)
                .map(facility -> {
                    RoomFacilityInfo roomFacilityInfo = RoomFacilityInfo.builder()
                            .facilityCategory(facility.getCategory())
                            .facilityName(facility.getFacilityName())
                            .facilityImageUrl(facility.getFacilityImageUrl())
                            .build();
                    return roomFacilityInfo;
                })
                .collect(Collectors.toList());

        // Category 별로 그룹화
        List<FacilitiesWithSameCategory> facilitiesWithSameCategories = new ArrayList<>();

        // distinct 한 Category 종류
        List<String> categories = facilities.stream()
                .map(RoomFacilityInfo::getFacilityCategory)
                .distinct()
                .collect(Collectors.toList());

        // Category 별로 묶어서 추가
        if(!categories.isEmpty()){
            for(int idx=0; idx<categories.size(); idx++){
                String category = categories.get(idx);

                // 같은 Category 를 갖는 시설 정보 리스트
                List<RoomFacilityInfo> roomFacilityInfos = facilities.stream()
                        .filter(roomFacilityInfo -> roomFacilityInfo.getFacilityCategory().equals(category))
                        .collect(Collectors.toList());

                FacilitiesWithSameCategory facilitiesWithCategory = new FacilitiesWithSameCategory();

                facilitiesWithCategory.setCategory(category);
                facilitiesWithCategory.setFacilities(roomFacilityInfos);

                facilitiesWithSameCategories.add(facilitiesWithCategory);
            }
        }

        return facilitiesWithSameCategories;
    }

}
