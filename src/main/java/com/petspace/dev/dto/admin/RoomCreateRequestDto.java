package com.petspace.dev.dto.admin;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class RoomCreateRequestDto {

    // TODO Thymeleaf 화면 상에서 값을 매핑 안해도 Valid 체크가 되지 않는다.
    // TODO 왜일까 ?

    // 서울특별시 인천광역시 부산광역시.. 경상도 경기도 전라도 제주도
    @NotBlank(message = "region 을 입력해주세요.")
    private String region;

    // city : 서울시 인천시 부산시 안산시 제주시 서귀포시 ~군. 저장시에는 행정구분단위 제외하고 이름만
    @NotBlank(message = "city 를 입력해주세요.")
    private String city;

    // district : 구/읍/면 (리는 제외). 저장시에는 행정구분 포함 !!
    @NotBlank(message = "district 를 입력해주세요.")
    private String district;

    // addressDetail : 상위 제외하고 나머지 주소
    @NotBlank(message = "addressDetail 을 입력해주세요.")
    private String addressDetail;

    @NotBlank(message = "latitude 를 입력해주세요.")
    private String latitude; // 위도

    @NotBlank(message = "longitude 를 입력해주세요.")
    private String longitude; // 경도

    @NotBlank(message = "roomName 을 입력해주세요.")
    private String roomName; // 숙소 이름

    @NotNull(message = "price 를 입력해주세요.")
    private int price; // 숙소 가격

    @NotNull(message = "maxGuest 를 입력해주세요.")
    private int maxGuest; // 최대 인원 수

    @NotNull(message = "maxPet 를 입력해주세요.")
    private int maxPet; // 최대 반려동물 수

    private String description; // 숙소 상세설명

    @NotNull(message = "checkinTime 를 입력해주세요.")
    private String checkinTime; // 체크인 시간

    @NotNull(message = "checkoutTime 를 입력해주세요.")
    private String checkoutTime; // 체크아웃 시간

}
