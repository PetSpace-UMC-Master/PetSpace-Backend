package com.petspace.dev.dto.room;

import com.petspace.dev.domain.*;
import com.petspace.dev.domain.image.RoomImage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Getter @Setter
public class RoomDetailResponseDto {

    // TODO isFavorite 추가 필요
    private Long roomId;
    private Long hostId;
    private String hostName;
    private String address;
    private String latitude; // 위도
    private String longitude; // 경도
    private String roomName;
    private int price;
    private int maxGuest;
    private int maxPet;
    private String roomDecription;
    private LocalDateTime checkinTime;
    private LocalDateTime checkoutTime;
    private double roomAverageScore; // Review AVG
    private long reviewCount; // Review COUNT
    private List<String> roomImageUrls;
    private List<RoomDetailReview> reviewPreviews;
    private List<RoomDetailFacility> facilities;

    public RoomDetailResponseDto(Room room){

        this.roomId = room.getId();
        this.hostId = room.getUser().getId();
        this.hostName = room.getUser().getUsername();
        // Room 의 주소 받아오기.
        Address address = room.getAddress();
        this.address = address.getCity() + " " + address.getAddressDetail();
        this.latitude = address.getLatitude();
        this.longitude = address.getLongitude();
        this.roomName = room.getRoomName();
        this.price = room.getPrice();
        this.maxGuest = room.getMaxGuest();
        this.maxPet = room.getMaxPet();
        this.roomDecription = room.getDescription();
        this.checkinTime = room.getCheckinTime();
        this.checkoutTime = room.getCheckoutTime();
        // 리뷰 평점 가져오기
        this.roomAverageScore = getRoomAverageScore(room);
        // 리뷰 개수 가져오기
        this.reviewCount = room.getReservation()
                .stream().map(Reservation::getReview)
                .count();
        // Room 의 Image 리스트에서 URL 만을 List<String> 으로 받아오기
        this.roomImageUrls = room.getRoomImages()
                .stream().map(RoomImage::getRoomImageUrl)
                .collect(Collectors.toList());
        // 리뷰 미리보기 5개 가져오기
        this.reviewPreviews = getRoomDetailReviews(room);
        // Room 의 편의시설 받아오기
        this.facilities = getRoomDetailFacilities(room);
    }

    /**
     * 리뷰 평점 구하기
     */
    private double getRoomAverageScore(Room room){
        double roomAverageScore = room.getReservation()
                .stream().map(Reservation::getReview)
                .filter(Objects::nonNull) // Review 가 nonNull 인 경우만
                .map(Review::getScore)
                .mapToDouble(n -> n)
                .average()
                .orElse(0);

        return Double.parseDouble(String.format("%.2f", roomAverageScore));
    }

    /**
     * 리뷰 미리보기 5개 가져오기
     */
    private List<RoomDetailReview> getRoomDetailReviews(Room room) {

        List<RoomDetailReview> reviewPreview = room.getReservation()
                .stream().map(Reservation::getReview)
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(Review::getId).reversed())
                .limit(5)
                .map(review ->{
                    Reservation reservation = review.getReservation();
                    RoomDetailReview roomDetailReviews = RoomDetailReview.builder()
                            .userId(reservation.getUser().getId())
                            .nickname(reservation.getUser().getNickname())
                            .score(review.getScore())
                            .createdAt(review.getCreatedAt())
                            .description(review.getContent())
                            .build();
                    return roomDetailReviews;
                })
                .collect(Collectors.toList());
        return reviewPreview;
    }

    /**
     * Room 의 편의시설 받아오기
     */
    private List<RoomDetailFacility> getRoomDetailFacilities(Room room) {

        List<RoomDetailFacility> facilities = room.getRoomFacilities()
                .stream().map(RoomFacility::getFacility)
                .limit(6)
                .map(facility -> {
                    RoomDetailFacility roomDetailFacilities = RoomDetailFacility.builder()
                            .facilityName(facility.getFacilityName())
                            .facilityImageUrl(facility.getFacilityImageUrl())
                            .build();
                    return roomDetailFacilities;
                })
                .collect(Collectors.toList());

        return facilities;
    }

}
