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

        // 카테고리별로 한 번의 loop
        while(!facilities.isEmpty()){
            RoomFacilityInfo roomFacilityInfo = facilities.get(0);

            // 카테고리에 해당하는 모든 편의시설 정보
            FacilitiesWithSameCategory facilitiesWithCategory = new FacilitiesWithSameCategory();
            // Category 지정
            facilitiesWithCategory.setCategory(roomFacilityInfo.getFacilityCategory());

            // Categroy 에 해당하는 모든 편의시설 추가
            List<RoomFacilityInfo> roomFacilityInfos = new ArrayList<>();

            for(int i=0; i<facilities.size(); i++){
                // Category 같으면 List 에 추가
                if(facilities.get(i).getFacilityCategory().equals(roomFacilityInfo.getFacilityCategory())){
                    roomFacilityInfos.add(facilities.get(i));
                    facilities.remove(i);
                    i--;
                }
            }

            facilitiesWithCategory.setFacilities(roomFacilityInfos);

            // (카테고리 + 해당 리스트) 를 리스트에 추가
            facilitiesWithSameCategories.add(facilitiesWithCategory);
        }

        return facilitiesWithSameCategories;
    }

}
