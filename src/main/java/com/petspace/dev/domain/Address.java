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

    // region : 서울만 예외처리 필요
    // e.g. 서울특별시(region) 서울시(region)
    @Column(length = 10, nullable = false)
    private String region;

    @Column(length = 10, nullable = false)
    private String city;

    @Column(length = 45, nullable = false)
    private String addressDetail;

    // TODO MAP API에 모든 숙박 정보를 가지고 있는 경우, nullable = false 제한
    private String latitude; // 위도
    private String longitude; // 경도
}
