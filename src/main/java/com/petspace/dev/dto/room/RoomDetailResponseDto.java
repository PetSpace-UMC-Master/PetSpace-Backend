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
    // TODO DTO 말고 다른 자료구조로 될까 ? VO

    // TODO 자료구조명 대신 복수형으로 변수명
    // TODO DTO 내부 DTO 이름 VO .. 어쩌구
    public RoomDetailResponseDto(Room room){

        this.roomId = room.getId();
        this.hostName = room.getUser().getUsername();
        // Room 의 주소 받아오기. TODO 주소 어느 형식으로 보내줄지 논의 필요
        // TODO 예외 처리 필요 -> 서울... region 안갖고와도 될 것 같던데?
        // TODO 두개만 합치고 컬럼 하나는 리전 그대로 주는 방식으로 예외처리?
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
    // TODO dtos??????? -> dto 자체가 data transfer object -> dto 맞지 않음 +++ room 붙힐 필요가? + VO 검색해볼것
    private List<RoomDetailReviewDto> getRoomDetailReviewDtos(Room room) {

//         Review Entity List 가져오기
        List<Review> reviewPreview = room.getReservation()
                .stream().map(Reservation::getReview)
                .sorted(Comparator.comparing(Review::getId).reversed())
                .limit(5)
                .collect(Collectors.toList());

        // TODO 스트림을 통해 줄일 수 있을 듯 함
        // 현재는 ID 가 높은 5개 리뷰가 출력되고, 업데이트가 아닌 생성 시간이 가장 최근인 5개 리뷰를 의미한다.
        Comparator<Review> comparator = (r1, r2) -> Long.valueOf(
                        r1.getId())
                .compareTo(r2.getId());

        Collections.sort(reviewPreview, comparator.reversed()); // TODO stream 쪽 order 찾아보기 // 리팩토링
        reviewPreview = reviewPreview.stream().limit(5).collect(Collectors.toList()); // stream limit 은 없을 때 Exception 발생하지 않음.

        // 5개 Review 엔티티를 필요한 정보로 DTO 화 하여 List 에 담는다.
        List<RoomDetailReviewDto> roomDetailReviewDtos = new ArrayList<>();
        if(!reviewPreview.isEmpty()){
        // TODO reviewPreview.stream().map(roomDetailReviewDtos::new).collect(Collectors.toList())
            for(Review review : reviewPreview){
                roomDetailReviewDtos.add(
                        RoomDetailReviewDto.builder()
                                .nickname(review.getReservation().getUser().getNickname())
                                .createdAt(review.getCreatedAt())
                                .description(review.getContent())
                                .build()
                );
            }
        }
        return roomDetailReviewDtos;
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
