package com.petspace.dev.util.input.room;

import lombok.Getter;

@Getter
public enum CategoryType {

    HOUSE(1L),
    CAMPSITE(2L),
    DOWNTOWN(3L),
    COUNTRY(4L),
    BEACH(5L);

    private final Long categoryId;

    CategoryType(Long id) {
        this.categoryId = id;
    }
}
