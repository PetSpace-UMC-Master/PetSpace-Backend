package com.petspace.dev.controller;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.user.auth.PrincipalDetails;
import com.petspace.dev.dto.reservation.ReservationCreateRequestDto;
import com.petspace.dev.dto.reservation.ReservationCreateResponseDto;
import com.petspace.dev.dto.reservation.ReservationReadResponseDto;
import com.petspace.dev.service.ReservationService;
import com.petspace.dev.util.BaseResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/app/reservations")
    public BaseResponse<ReservationCreateResponseDto> createReservation(@AuthenticationPrincipal PrincipalDetails principalDetail,
                                                                        @RequestParam("roomId") Long roomId,
                                                                        @RequestBody ReservationCreateRequestDto dto) {
        Long userId = principalDetail.getId();
        ReservationCreateResponseDto responseDto = reservationService.save(userId, roomId, dto);
        return new BaseResponse<>(responseDto);
    }

    @GetMapping("/app/reservations")
    public BaseResponse<Object> readReservationByUserId(@AuthenticationPrincipal PrincipalDetails principalDetail) {
        Long userId = principalDetail.getId();
        List<ReservationReadResponseDto> reservationReadResponseDtoList = reservationService.readUpComingReservation(userId);
        return new BaseResponse<>(reservationReadResponseDtoList);
    }

    @GetMapping("/app/reservations/terminate")
    public BaseResponse<Object> readTerminateReservationByUserId(@AuthenticationPrincipal PrincipalDetails principalDetail) {
        Long userId = principalDetail.getId();
        List<ReservationReadResponseDto> reservationReadResponseDtoList = reservationService.readTerminateReservation(userId);
        return new BaseResponse<>(reservationReadResponseDtoList);
    }

    @PatchMapping("app/reservations/{reservationId}/delete")
    public BaseResponse<Object> deleteReservation(@PathVariable Long reservationId) {
        Long id = reservationService.delete(reservationId);
        return new BaseResponse<>(id);
    }
}
