package com.petspace.dev.controller;

import com.petspace.dev.domain.user.auth.PrincipalDetails;
import com.petspace.dev.dto.reservation.ReservationCreateRequestDto;
import com.petspace.dev.dto.reservation.ReservationCreateResponseDto;
import com.petspace.dev.dto.reservation.ReservationDeleteResponseDto;
import com.petspace.dev.dto.reservation.ReservationReadResponseDto;
import com.petspace.dev.service.ReservationService;
import com.petspace.dev.util.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "Reservation 생성", description = "ReservationCreateRequestDto 와 roomId, principalDetail 를 이용해 reservation 레코드를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "1.해당 사용자가 존재하지 않습니다.\n\n2.해당 숙소가 존재하지 않습니다.\n\n3.숙소이용 정원이 초과 되었습니다.\n\n4.숙소 동반 가능한 반려동물 수가 초과 되었습니다.\n\n5.예약 불가능한 날짜 입니다.")

    })
    @PostMapping("/app/reservations")
    public BaseResponse<ReservationCreateResponseDto> createReservation(@AuthenticationPrincipal PrincipalDetails principalDetail,
                                                                        @Parameter(name = "roomId", description = "room 의 id", in = ParameterIn.QUERY) @RequestParam("roomId") Long roomId,
                                                                        @RequestBody ReservationCreateRequestDto dto) {
        Long userId = principalDetail.getId();
        ReservationCreateResponseDto responseDto = reservationService.saveReservation(userId, roomId, dto);
        return new BaseResponse<>(responseDto);
    }

    @Operation(summary = "Reservation 조회", description = "principalDetail 을 이용해 현재날짜를 기준으로 다가오는 reservation 을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "400", description = "해당 사용자가 존재하지 않습니다."),
    })
    @GetMapping("/app/reservations")
    public BaseResponse<List<ReservationReadResponseDto>> readReservationByUserId(@AuthenticationPrincipal PrincipalDetails principalDetail) {
        Long userId = principalDetail.getId();
        List<ReservationReadResponseDto> reservationReadResponseDtoList = reservationService.readUpComingReservation(userId);
        return new BaseResponse<>(reservationReadResponseDtoList);
    }


    @Operation(summary = "Reservation 조회", description = "principalDetail 을 이용해 현재날짜를 기준으로 완료된 reservation 을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "200", description = "해당 사용자가 존재하지 않습니다."),
    })
    @GetMapping("/app/reservations/terminate")
    public BaseResponse<Object> readTerminateReservationByUserId(@AuthenticationPrincipal PrincipalDetails principalDetail) {
        Long userId = principalDetail.getId();
        List<ReservationReadResponseDto> reservationReadResponseDtoList = reservationService.readTerminateReservation(userId);
        return new BaseResponse<>(reservationReadResponseDtoList);
    }

    @Operation(summary = "Reservation 삭제", description = "Soft Delete Reservation API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청에 성공하였습니다."),
            @ApiResponse(responseCode = "200", description = "유효하지 않은 예약입니다."),
            @ApiResponse(responseCode = "200", description = "존재하지 않는 예약입니다."),
    })
    @PatchMapping("app/reservations/{reservationId}/delete")
    public BaseResponse<ReservationDeleteResponseDto> deleteReservation(@AuthenticationPrincipal PrincipalDetails principalDetail, @PathVariable Long reservationId) {
        Long userId = principalDetail.getId();
        ReservationDeleteResponseDto dto = reservationService.deleteReservation(userId, reservationId);
        return new BaseResponse<>(dto);
    }
}
