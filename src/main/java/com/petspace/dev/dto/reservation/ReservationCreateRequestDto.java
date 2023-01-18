package com.petspace.dev.dto.reservation;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Room;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ReservationCreateRequestDto {

    //private int totalPrice;
    private int totalGuest;
    @DateTimeFormat(pattern = "yyyy-mm-dd'T'HH:mm:ss")
    private LocalDateTime startDate;
    @DateTimeFormat(pattern = "yyyy-mm-dd'T'HH:mm:ss")
    private LocalDateTime endDate;

    //dto -> entity
    public Reservation toEntity() {
        return Reservation.builder()
                .reservationCode(UUID.randomUUID().toString()) //reservationCode 생성
                .totalPrice(0) //전체가격을 0으로 초기화 후 따로 계산한다.
                .totalGuest(totalGuest)
                .startDate(startDate)
                .endDate(endDate)
                .status(Status.ACTIVE)
                .build();
    }
}
