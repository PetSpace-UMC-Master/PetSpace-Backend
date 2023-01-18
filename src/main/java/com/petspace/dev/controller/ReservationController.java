package com.petspace.dev.controller;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.dto.reservation.ReservationCreateRequestDto;
import com.petspace.dev.dto.reservation.ReservationCreateResponseDto;
import com.petspace.dev.dto.reservation.ReservationReadResponseDto;
import com.petspace.dev.service.ReservationService;
import com.petspace.dev.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/api/reservations/{userId}/{roomId}")
    public ResponseEntity<ReservationCreateResponseDto> make(@PathVariable Long userId, @PathVariable Long roomId, @RequestBody ReservationCreateRequestDto dto) {
        Reservation reservation = reservationService.makeReservation(userId, roomId, dto);
        return ResponseEntity.ok(ReservationCreateResponseDto.builder()
                .reservationCode(reservation.getReservationCode())
                .totalPrice(reservation.getTotalPrice())
                .totalGuest(reservation.getTotalGuest())
                .startDate(reservation.getStartDate())
                .endDate(reservation.getEndDate())
                .build());
    }

    @GetMapping("/api/reservations/{userId}")
    public BaseResponse<Object> readReservationByUserId(@PathVariable Long userId) {
        List<ReservationReadResponseDto> reservationReadResponseDtoList = reservationService.readReservation(userId);
        return new BaseResponse<>(reservationReadResponseDtoList);
    }
}
