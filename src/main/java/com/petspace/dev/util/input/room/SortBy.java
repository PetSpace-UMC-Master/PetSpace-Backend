package com.petspace.dev.util.input.room;

import lombok.Getter;

@Getter
public enum SortBy {

    ID_DESC("id", "DESC"),
    PRICE_DESC("price", "DESC"),
    PRICE_ASC("price", "ASC"),
    REVIEW_COUNT_DESC("review_count", "DESC"),
    AVERAGE_REVIEW_SCORE_DESC("average_review_score", "DESC");

    private final String sortType;
    private final String orderType;

    SortBy(String sortType, String orderType) {
        this.sortType = sortType;
        this.orderType = orderType;
    }
}
