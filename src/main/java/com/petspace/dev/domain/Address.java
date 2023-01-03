package com.petspace.dev.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

    // TODO 괜찮은 변수명으로 바꿔야돼요!
    private String address1; // 시
    private String address2; // 구/읍/면
    private String address3; // 동/리

    private String latitude; // 위도
    private String longitude; // 경도
}
