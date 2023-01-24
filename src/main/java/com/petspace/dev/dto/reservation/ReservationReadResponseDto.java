package com.petspace.dev.dto.reservation;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.image.RoomImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ReservationReadResponseDto {

    private String reservationCode;
    private String roomName;
    private List<RoomImage> roomImages;
    private LocalDate startDate;
    private LocalDate endDate;
    private int remainingDays;

    public ReservationReadResponseDto(Reservation reservation) {
        reservationCode = reservation.getReservationCode();
        roomName = reservation.getRoom().getRoomName();
        roomImages = reservation.getRoom().getRoomImages();
        startDate = reservation.getStartDate().toLocalDate();
        endDate = reservation.getEndDate().toLocalDate();
        this.setRemainingDays();
    }
    public void setRemainingDays() {
        Period period = Period.between(LocalDate.now(), startDate);
        remainingDays = period.getDays();
    }
}

