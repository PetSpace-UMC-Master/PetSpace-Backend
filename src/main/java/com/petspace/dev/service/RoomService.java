package com.petspace.dev.service;

import com.petspace.dev.domain.*;
import com.petspace.dev.domain.image.RoomImage;
import com.petspace.dev.dto.room.RoomDetailFacilityDto;
import com.petspace.dev.dto.room.RoomDetailResponseDto;
import com.petspace.dev.dto.room.RoomDetailReviewDto;
import com.petspace.dev.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;

    // TODO
//    public int createRoom(RoomCreateRequestDto roomCreateRequestDto){
//        int result = 0;
//        Room room = roomCreateRequestDto.toEntity();
//        roomRepository.save(room);
//        return result;
//    }

    @Transactional
    public RoomDetailResponseDto getRoomDetail(Long roomId) {

        /**
         * Room 가져오기. 현재는 Null 값일 수 없음을 가정한다.
         */
        Optional<Room> roomList = roomRepository.findById(roomId);
        Room room = roomList.get();
        // TODO roomId Error Handling -> 'Exception 관련 commit by 상진' pull 이후에 진행.

        // Room 의 Image 리스트에서 URL 만을 List<String> 으로 받아오기
        List<String> imageUrlList = getRoomImageUrlbyId(room);

        // Room 의 주소 받아오기. TODO 주소 어느 형식으로 보내줄지 논의 필요
        String address = room.getAddress().getRegion() + " " + room.getAddress().getCity() + " " + room.getAddress().getAddressDetail();

        // 리뷰 평점 가져오기
        double review_rate = getRoomReviewAvgRate(room);

        // 리뷰 개수 가져오기
        long review_count = getRoomReviewCount(room);

        // 리뷰 미리보기 5개 가져오기
        List<RoomDetailReviewDto> roomDetailReviewDtos = getRoomDetailReviewDtos(room);

        // Room 의 편의시설 받아오기
        List<RoomDetailFacilityDto> facilityInfoDtos = getRoomDetailFacilityDtos(room);

        return RoomDetailResponseDto
                .builder()
                .id(room.getId())
                .host(room.getUser().getUsername())
                .roomImageUrl(imageUrlList)
                .address(address)
                .name(room.getRoomName())
                .price(room.getPrice())
                .maxGuest(room.getMaxGuest())
                .maxPet(room.getMaxPet())
                .decription(room.getDescription())
                .checkinTime(room.getCheckinTime()) // TODO CheckIn 시간형식 논의
                .checkoutTime(room.getCheckoutTime()) // TODO CheckOut 시간형식 논의
                .rating(review_rate)
                .reviewCount(review_count)
                .reviewPreview(roomDetailReviewDtos)
                .facilities(facilityInfoDtos)
                .build();
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
        for(Facility fc : facilities){
            facilityInfoDtos.add(
                    RoomDetailFacilityDto.builder()
                    .facilityName(fc.getFacilityName())
                    .facilityImageUrl(fc.getFacilityImageUrl())
                    .build()
            );
        }
        return facilityInfoDtos;
    }


    /**
     * 리뷰 미리보기 5개 가져오기
     */
    private List<RoomDetailReviewDto> getRoomDetailReviewDtos(Room room) {

        // Review Entity List 가져오기
        List<Review> reviewPreview = room.getReservation()
                .stream().map(Reservation::getReview)
                .collect(Collectors.toList());

        // 현재는 ID 가 높은 5개 리뷰가 출력되고, 업데이트가 아닌 생성 시간이 가장 최근인 5개 리뷰를 의미한다.
        Comparator<Review> comparator = (r1, r2) -> Long.valueOf(
                        r1.getId())
                .compareTo(r2.getId());

        Collections.sort(reviewPreview, comparator.reversed());
        reviewPreview = reviewPreview.stream().limit(5).collect(Collectors.toList()); // stream limit 은 없을 때 Exception 발생하지 않음.

        // 5개 Review 엔티티를 필요한 정보로 DTO 화 하여 List 에 담는다.
        List<RoomDetailReviewDto> roomDetailReviewDtos = new ArrayList<>();
        if(!reviewPreview.isEmpty()){
            for(Review rv : reviewPreview){
                roomDetailReviewDtos.add(
                        RoomDetailReviewDto.builder()
                                .nickname(rv.getReservation().getUser().getNickname())
                                .createdAt(rv.getCreatedAt())
                                .description(rv.getContent())
                                .build()
                );
            }
        }
        return roomDetailReviewDtos;
    }

    /**
     * 리뷰 개수 가져오기
     */
    private long getRoomReviewCount(Room room) {
        long review_count = room.getReservation()
                .stream().map(Reservation::getReview)
                .count();
        return review_count;
    }

    /**
     * 리뷰 평점 가져오기
     */
    private double getRoomReviewAvgRate(Room room) {
        double review_rate = room.getReservation()
                .stream().map(Reservation::getReview)
                .filter(Objects::nonNull) // Review 가 nonNull 인 경우만
                .map(Review::getScore)
                .mapToDouble(n -> n)
                .average()
                .orElse(0);
        return review_rate;
    }

    /**
     * Room 의 Image 리스트에서 URL 만을 List<String> 으로 받아오기
     */
    private List<String> getRoomImageUrlbyId(Room room) {
        List<String> imageUrlList = room.getRoomImages()
                .stream().map(RoomImage::getRoomImageUrl)
                .collect(Collectors.toList());
        return imageUrlList;
    }

}
