package com.petspace.dev.dto.room;

import com.petspace.dev.domain.*;
import com.petspace.dev.domain.image.RoomImage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Getter @Setter
public class RoomDetailResponseDto {

    private Long roomId;
    private boolean isFavorite;
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
    private String checkinTime;
    private String checkoutTime;
    private double roomAverageScore; // Review AVG
    private long reviewCount; // Review COUNT
    private List<String> roomImageUrls;
    private List<RoomDetailReview> reviewPreviews;
    private List<RoomFacilityInfo> facilities;

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
        this.checkinTime = room.getCheckinTime().format(DateTimeFormatter.ofPattern("HH시 mm분"));;
        this.checkoutTime = room.getCheckoutTime().format(DateTimeFormatter.ofPattern("HH시 mm분"));
        // 리뷰 평점 가져오기
        this.roomAverageScore = getRoomAverageScore(room);
        // 리뷰 개수 가져오기
        this.reviewCount = room.getReviews().stream()
                .filter(Objects::nonNull)
                .filter(r -> r.getStatus().equals(Status.ACTIVE))
                .collect(Collectors.toList()).size();
        // Room 의 Image 리스트에서 URL 만을 List<String> 으로 받아오기
        // TODO 추후 RoomImage Entity List 로 변경. 순환참조 문제 해결. Room Entity 내 List<String> 으로 갖는 것과 다를 바 없다.
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
        double roomAverageScore = room.getReviews()
                .stream()
                .filter(Objects::nonNull)
                .filter(r -> r.getStatus().equals(Status.ACTIVE))
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
                .filter(review -> review.getStatus().equals(Status.ACTIVE))
                .sorted(Comparator.comparing(Review::getId).reversed())
                .limit(5)
                .map(review ->{
                    Reservation reservation = review.getReservation();
                    RoomDetailReview roomDetailReviews = RoomDetailReview.builder()
                            .userId(reservation.getUser().getId())
                            .profileImage(reservation.getUser().getProfileImage())
                            .nickname(reservation.getUser().getNickname())
                            .score(review.getScore())
                            .createdAt(calculateCreatedDateFromNow(review.getCreatedAt()))
                            .description(review.getContent())
                            .build();
                    return roomDetailReviews;
                })
                .collect(Collectors.toList());
        return reviewPreview;
    }

    /**
     * 리뷰 시간 계산
     */
    private String calculateCreatedDateFromNow(LocalDateTime localDateTime){

        LocalDateTime now = LocalDateTime.now();

        if(now.getYear() == localDateTime.getYear()){
            if(now.getMonth() == localDateTime.getMonth()){
                if(now.getDayOfMonth() == localDateTime.getDayOfMonth()){
                    return "오늘";
                }else{
                    return now.getDayOfMonth() - localDateTime.getDayOfMonth() + "일 전";
                }
            }else{
                return now.getMonthValue() - localDateTime.getMonthValue() + "개월 전";
            }
        }else{
            return now.getYear() - localDateTime.getYear() + "년 전";
        }

    }

    /**
     * Room 의 편의시설 6개 받아오기
     */
    private List<RoomFacilityInfo> getRoomDetailFacilities(Room room) {

        List<RoomFacilityInfo> facilities = room.getRoomFacilities()
                .stream().map(RoomFacility::getFacility)
                .limit(6)
                .map(facility -> {
                    RoomFacilityInfo roomDetailFacilities = RoomFacilityInfo.builder()
                            .facilityCategory(facility.getCategory())
                            .facilityName(facility.getFacilityName())
                            .facilityImageUrl(facility.getFacilityImageUrl())
                            .build();
                    return roomDetailFacilities;
                })
                .collect(Collectors.toList());

        return facilities;
    }

}
