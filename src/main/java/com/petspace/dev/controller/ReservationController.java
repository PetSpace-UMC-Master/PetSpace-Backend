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

    @PostMapping("/app/reservations/{userId}/{roomId}")
    public BaseResponse<ReservationCreateResponseDto> createReservation(@PathVariable Long userId, @PathVariable Long roomId, @RequestBody ReservationCreateRequestDto dto) {
        ReservationCreateResponseDto responseDto = reservationService.save(userId, roomId, dto);
        return new BaseResponse<>(responseDto);
    }

    @GetMapping("/app/reservations/{userId}")
    public BaseResponse<Object> readReservationByUserId(@PathVariable Long userId) {
        List<ReservationReadResponseDto> reservationReadResponseDtoList = reservationService.readUpComingReservation(userId);
        return new BaseResponse<>(reservationReadResponseDtoList);
    }

    @GetMapping("/app/reservations/{userId}/terminate")
    public BaseResponse<Object> readTerminateReservationByUserId(@PathVariable Long userId) {
        List<ReservationReadResponseDto> reservationReadResponseDtoList = reservationService.readTerminateReservation(userId);
        return new BaseResponse<>(reservationReadResponseDtoList);
    }

    @PatchMapping("app/reservations/{reservationId}/delete")
    public BaseResponse<Object> deleteReservation(@PathVariable Long reservationId) {
        Long id = reservationService.delete(reservationId);
        return new BaseResponse<>(id);
    }
}
