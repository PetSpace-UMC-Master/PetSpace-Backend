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

    private Long roomId;
    private String hostName; // User Entity의 name
    private String address; // Address Entity 데이터 가공
    private String roomName;
    private int price; //
    private int maxGuest;
    private int maxPet;
    private String roomDecription;
    private LocalDateTime checkinTime;
    private LocalDateTime checkoutTime;
    private double roomAverageScore; // Review AVG
    private long reviewCount; // Review COUNT
    private List<String> roomImageUrl;
    private List<RoomDetailReviewDto> reviewPreview;
    private List<RoomDetailFacilityDto> facilities;

    // TODO 자료구조명 대신 복수형으로 변수명
    public RoomDetailResponseDto(Room room){

        this.roomId = room.getId();
        this.hostName = room.getUser().getUsername();
        // Room 의 주소 받아오기. TODO 주소 어느 형식으로 보내줄지 논의 필요
        this.address = room.getAddress().getCity() + " " + room.getAddress().getAddressDetail();
        this.roomName = room.getRoomName();
        this.price = room.getPrice();
        this.maxGuest = room.getMaxGuest();
        this.maxPet = room.getMaxPet();
        this.roomDecription = room.getDescription();
        this.checkinTime = room.getCheckinTime(); // TODO CheckIn 시간형식 논의
        this.checkoutTime = room.getCheckoutTime(); // TODO CheckIn 시간형식 논의
        // 리뷰 평점 가져오기
        this.roomAverageScore = room.getReservation()// TODO 소수점 2자리
                .stream().map(Reservation::getReview)
                .filter(Objects::nonNull) // Review 가 nonNull 인 경우만
                .map(Review::getScore)
                .mapToDouble(n -> n)
                .average()
                .orElse(0);
        // 리뷰 개수 가져오기
        this.reviewCount = room.getReservation()
                .stream().map(Reservation::getReview)
                .count();
        // Room 의 Image 리스트에서 URL 만을 List<String> 으로 받아오기
        this.roomImageUrl = room.getRoomImages()
                .stream().map(RoomImage::getRoomImageUrl)
                .collect(Collectors.toList());
        // 리뷰 미리보기 5개 가져오기
        this.reviewPreview = getRoomDetailReviewDtos(room);
        // Room 의 편의시설 받아오기
        this.facilities = getRoomDetailFacilityDtos(room);
    }

    /**
     * 리뷰 미리보기 5개 가져오기
     */
    private List<RoomDetailReviewDto> getRoomDetailReviewDtos(Room room) {

        List<RoomDetailReviewDto> reviewPreview = room.getReservation()
                .stream().map(Reservation::getReview)
                .sorted(Comparator.comparing(Review::getId).reversed())
                .limit(5)
                .map(review ->{
                    RoomDetailReviewDto roomDetailReviewDto = RoomDetailReviewDto.builder()
                            .userId(review.getReservation().getUser().getId())
                            .nickname(review.getReservation().getUser().getNickname())
                            .createdAt(review.getCreatedAt())
                            .description(review.getContent())
                            .build();
                    return roomDetailReviewDto;
                })
                .collect(Collectors.toList());

        return reviewPreview;
    }

    /**
     * Room 의 편의시설 받아오기
     */
    private List<RoomDetailFacilityDto> getRoomDetailFacilityDtos(Room room) {

        // Facility Entity List 가져오기
        List<Facility> facilities = room.getRoomFacilities()
                .stream().map(RoomFacility::getFacility)
                .collect(Collectors.toList());

        // Room 엔티티를 필요한 정보로 DTO 화 하여 List 에 담는다.
        List<RoomDetailFacilityDto> facilityInfoDtos = new ArrayList<>();
        if(!facilityInfoDtos.isEmpty()) {
            for (Facility fc : facilities) {
                facilityInfoDtos.add(
                        RoomDetailFacilityDto.builder()
                                .facilityName(fc.getFacilityName())
                                .facilityImageUrl(fc.getFacilityImageUrl())
                                .build()
                );
            }
        }
        return facilityInfoDtos;
    }


}
