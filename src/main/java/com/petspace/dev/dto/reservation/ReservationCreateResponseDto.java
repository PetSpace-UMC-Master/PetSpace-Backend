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

    private Long reservationId;

    public ReservationCreateResponseDto(Reservation reservation) {
        reservationId = reservation.getId();
    }
}
