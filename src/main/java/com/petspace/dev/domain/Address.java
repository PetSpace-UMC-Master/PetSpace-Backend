package com.petspace.dev.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    // 서울특별시 인천광역시 부산광역시.. 경상도 경기도 전라도 제주도
    @Column(length = 10, nullable = false)
    private String region;

    // city : 서울시 인천시 부산시 안산시 제주시 서귀포시 ~군. 저장시에는 행정구분단위 제외하고 이름만
    @Column(length = 10, nullable = false)
    private String city;

    // district : 구/읍/면 (리는 제외). 저장시에는 행정구분 포함 !!
    @Column(length = 10, nullable = false)
    private String district;

    // addressDetail : 상위 제외하고 나머지 주소
    @Column(length = 45, nullable = false)
    private String addressDetail;

    // TODO MAP API에 모든 숙박 정보를 가지고 있는 경우, nullable = false 제한
    private String latitude; // 위도
    private String longitude; // 경도
}
