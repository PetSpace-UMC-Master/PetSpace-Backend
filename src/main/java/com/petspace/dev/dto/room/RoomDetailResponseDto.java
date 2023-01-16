package com.petspace.dev.dto.room;

import com.petspace.dev.domain.Facility;
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
    private List<Review> reviewPreview;
    private List<Facility> facilities;


//    @Builder
//    public RoomDetailResponseDto(Long id, String host, List<String> roomImageUrl, String address,
//                                 String name, int price, int maxGuest, int maxPet, String decription,
//                                 LocalDateTime checkinTime, LocalDateTime checkoutTime, double rating,
//                                 long reviewCount, List<Review> review_preview, List<Facility> facilityUrl) {
//        this.id = id;
//        this.host = host;
//        this.roomImageUrl = roomImageUrl;
//        this.address = address;
//        this.name = name;
//        this.price = price;
//        this.maxGuest = maxGuest;
//        this.maxPet = maxPet;
//        this.decription = decription;
//        this.checkinTime = checkinTime;
//        this.checkoutTime = checkoutTime;
//        this.rating = rating;
//        this.reviewCount = reviewCount;
//        this.reviewPreview = review_preview;
//        this.facilityUrl = facilityUrl;
//    }

}
