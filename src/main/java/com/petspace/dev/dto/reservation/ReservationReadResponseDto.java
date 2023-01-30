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
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Builder
@AllArgsConstructor
public class ReservationReadResponseDto {

    private String reservationCode;
    private Long roomId;
    private boolean isReviewCreated;
    private String roomName;
    private List<String> roomImageUrls;
    private LocalDate startDate;
    private LocalDate endDate;
    private int remainingDays;

    //todo roomimage 말고 그냥 url만 넘기기
    public ReservationReadResponseDto(Reservation reservation) {
        reservationCode = reservation.getReservationCode();
        roomId = reservation.getRoom().getId();
        isReviewCreated = reservation.isReviewCreated();
        roomName = reservation.getRoom().getRoomName();
        roomImageUrls = reservation.getRoom().getRoomImages().stream()
                .map(roomImage -> roomImage.getRoomImageUrl())
                .collect(Collectors.toList());
        // roomImage가 5개 이상인 경우 5개까지만 출력
        if(roomImageUrls.size() > 5) {
            roomImageUrls = roomImageUrls.subList(0, 5);
        }
        startDate = reservation.getStartDate().toLocalDate();
        endDate = reservation.getEndDate().toLocalDate();
        remainingDays = Period.between(LocalDate.now(), startDate).getDays();
    }
}

