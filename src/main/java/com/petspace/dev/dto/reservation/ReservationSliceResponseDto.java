package com.petspace.dev.dto.reservation;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ReservationSliceResponseDto {

    List<ReservationReadResponseDto> reservations;
    private long page;
    @JsonProperty(value = "isLast")
    private Boolean isLast;
}
