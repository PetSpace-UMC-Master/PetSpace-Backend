package com.petspace.dev.dto.reservation;

import com.petspace.dev.domain.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Schema(description = "예약 삭제 응답 dto")
@Getter
@Builder
@AllArgsConstructor
public class ReservationDeleteResponseDto {
    @Schema(description = "삭제된 reservationId")
    private Long reservationId;

    public ReservationDeleteResponseDto(Reservation reservation) {
        reservationId = reservation.getId();
    }
}
