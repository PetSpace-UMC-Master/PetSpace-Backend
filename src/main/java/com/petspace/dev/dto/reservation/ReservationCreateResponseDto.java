package com.petspace.dev.dto.reservation;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ReservationCreateResponseDto {

    private String reservationCode;
    private int totalPrice;
    private int totalGuest;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
