package com.petspace.dev.dto.room;

import com.petspace.dev.domain.Review;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter
@Builder
public class RoomDetailResponseDto {

    private Long id;
    private String host; // User Entity의 name
    private List<String> roomImageUrl;
    private String address; // Address Entity 데이터 가공
    private String name;
    private int price; //
    private int maxGuest;
    private int maxPet;
    private String decription;
    private LocalDateTime checkinTime;
    private LocalDateTime checkoutTime;
    private double rating; // Review AVG
    private long reviewCount; // Review COUNT
    private List<RoomDetailReviewDto> reviewPreview;
    private List<RoomDetailFacilityDto> facilities;

}
