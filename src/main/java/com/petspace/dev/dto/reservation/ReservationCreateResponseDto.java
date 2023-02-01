package com.petspace.dev.dto.reservation;

import com.petspace.dev.domain.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Schema(description = "예약 생성 응답 dto")
@Getter
@Builder
@AllArgsConstructor
public class ReservationCreateResponseDto {

    @Schema(description = "생성된 reservationId", example = "1")
    private Long reservationId;

    public ReservationCreateResponseDto(Reservation reservation) {
        reservationId = reservation.getId();
    }
}
