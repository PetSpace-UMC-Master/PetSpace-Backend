package com.petspace.dev.dto.reservation;

import com.petspace.dev.domain.Reservation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class ReservationCreateResponseDto {

    private String reservationCode;
    private int totalPrice;
    private int totalGuest;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public ReservationCreateResponseDto(Reservation reservation) {
        reservationCode = reservation.getReservationCode();
        totalPrice = reservation.getTotalPrice();
        totalGuest = reservation.getTotalGuest();
        startDate = reservation.getStartDate();
        endDate = reservation.getEndDate();
    }
}
