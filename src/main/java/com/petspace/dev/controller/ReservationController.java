package com.petspace.dev.controller;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.user.auth.PrincipalDetails;
import com.petspace.dev.dto.reservation.ReservationCreateRequestDto;
import com.petspace.dev.dto.reservation.ReservationCreateResponseDto;
import com.petspace.dev.dto.reservation.ReservationReadResponseDto;
import com.petspace.dev.service.ReservationService;
import com.petspace.dev.util.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    // todo swagger responseCode 다 200으로 바꾸고 schema 검색해서 알맞게 바꾸기!!
    @Operation(summary = "Reservation Post", description = "Reservation Post API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2030", description = "해당 사용자가 존재하지 않습니다."),
            @ApiResponse(responseCode = "2031", description = "해당 숙소가 존재하지 않습니다."),
    })
    @PostMapping("/app/reservations")
    public BaseResponse<ReservationCreateResponseDto> createReservation(@AuthenticationPrincipal PrincipalDetails principalDetail,
                                                                        @RequestParam("roomId") Long roomId,
                                                                        @RequestBody ReservationCreateRequestDto dto) {
        Long userId = principalDetail.getId();
        ReservationCreateResponseDto responseDto = reservationService.saveReservation(userId, roomId, dto);
        return new BaseResponse<>(responseDto);
    }

    @Operation(summary = "Reservation Get", description = "Read Upcoming Reservation API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2030", description = "해당 사용자가 존재하지 않습니다."),
    })
    @GetMapping("/app/reservations")
    public BaseResponse<Object> readReservationByUserId(@AuthenticationPrincipal PrincipalDetails principalDetail) {
        Long userId = principalDetail.getId();
        List<ReservationReadResponseDto> reservationReadResponseDtoList = reservationService.readUpComingReservation(userId);
        return new BaseResponse<>(reservationReadResponseDtoList);
    }


    @Operation(summary = "Reservation Get", description = "Read terminate Reservation API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2030", description = "해당 사용자가 존재하지 않습니다."),
    })
    @GetMapping("/app/reservations/terminate")
    public BaseResponse<Object> readTerminateReservationByUserId(@AuthenticationPrincipal PrincipalDetails principalDetail) {
        Long userId = principalDetail.getId();
        List<ReservationReadResponseDto> reservationReadResponseDtoList = reservationService.readTerminateReservation(userId);
        return new BaseResponse<>(reservationReadResponseDtoList);
    }

    @Operation(summary = "Reservation Patch", description = "Soft Delete Reservation API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "1000", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "2033", description = "유효하지 않은 예약입니다."),
            @ApiResponse(responseCode = "2034", description = "존재하지 않는 예약입니다."),
    })
    @PatchMapping("app/reservations/{reservationId}/delete")
    public BaseResponse<Long> deleteReservation(@AuthenticationPrincipal PrincipalDetails principalDetail, @PathVariable Long reservationId) {
        Long id = reservationService.deleteReservation(reservationId);
        return new BaseResponse<>(id);
    }
}
