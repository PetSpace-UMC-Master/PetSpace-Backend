package com.petspace.dev.util.input.room;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RegionType {

    SEOUL("서울"),
    JEJUDO("제주도"),
    BUSAN("부산"),
    ULSAN("울산"),
    GWANGJU("광주"),
    DAEGU("대구"),
    DAEJEON("대전"),
    INCHEON("인천"),

    GYEONGGIDO("경기도"),
    GANGWONDO("강원도"),
    CHUNGCHENOGDO("충청도"),
    JEOLLADO("전라도"),
    GYEONGSANGDO("경상도");

    private final String korRegionName;

    public String getKorRegionName() {
        return korRegionName;
    }
}
