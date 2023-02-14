package com.petspace.dev.controller;

import com.petspace.dev.domain.user.auth.PrincipalDetails;
import com.petspace.dev.dto.reservation.ReservationCreateRequestDto;
import com.petspace.dev.dto.reservation.ReservationCreateResponseDto;
import com.petspace.dev.dto.reservation.ReservationDeleteResponseDto;
import com.petspace.dev.dto.reservation.ReservationSliceResponseDto;
import com.petspace.dev.service.ReservationService;
import com.petspace.dev.util.BaseResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ReservationController {

    private final ReservationService reservationService;

    @Operation(summary = "Reservation 생성", description = "ReservationCreateRequestDto 와 roomId, principalDetail 를 이용해 reservation 레코드를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패 - 잘못된 토큰, 혹은 만료된 토큰을 통해 호출된 경우"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PostMapping("/app/reservations")
    public BaseResponse<ReservationCreateResponseDto> createReservation(@AuthenticationPrincipal PrincipalDetails principalDetail,
                                                                        @Parameter(name = "roomId", description = "room 의 id", in = ParameterIn.QUERY) @RequestParam("roomId") Long roomId,
                                                                        @RequestBody ReservationCreateRequestDto dto) {
        Long userId = principalDetail.getId();
        ReservationCreateResponseDto responseDto = reservationService.saveReservation(userId, roomId, dto);
        return new BaseResponse<>(responseDto);
    }

    @Operation(summary = "Reservation 삭제", description = "Soft Delete Reservation API Doc")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패 - 잘못된 토큰, 혹은 만료된 토큰을 통해 호출된 경우"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @PatchMapping("app/reservations/{reservationId}/delete")
    public BaseResponse<ReservationDeleteResponseDto> deleteReservation(@AuthenticationPrincipal PrincipalDetails principalDetail, @PathVariable Long reservationId) {
        Long userId = principalDetail.getId();
        ReservationDeleteResponseDto dto = reservationService.deleteReservation(userId, reservationId);
        return new BaseResponse<>(dto);
    }

    @Operation(summary = "Reservation 조회", description = "principalDetail 을 이용해 현재날짜를 기준으로 다가오는 reservation 을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패 - 잘못된 토큰, 혹은 만료된 토큰을 통해 호출된 경우"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("app/reservations")
    public BaseResponse readAllUpcomingReservations(@AuthenticationPrincipal PrincipalDetails principalDetail,
                                            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                            @RequestParam(value = "size", required = false, defaultValue = "5") int size) {

        Long userId = principalDetail.getId();
        PageRequest pageRequest = PageRequest.of(page, size);
        ReservationSliceResponseDto responseDto = reservationService.findAllUpcomingReservationByPage(userId, pageRequest);
        return new BaseResponse<>(responseDto);
    }

    @Operation(summary = "Reservation 조회", description = "principalDetail 을 이용해 현재날짜를 기준으로 완료된 reservation 을 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "요청 성공"),
            @ApiResponse(responseCode = "401", description = "회원 인증 실패 - 잘못된 토큰, 혹은 만료된 토큰을 통해 호출된 경우"),
            @ApiResponse(responseCode = "500", description = "서버 오류")
    })
    @GetMapping("app/reservations/terminate")
    public BaseResponse readAllTerminateReservations(@AuthenticationPrincipal PrincipalDetails principalDetail,
                                            @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                            @RequestParam(value = "size", required = false, defaultValue = "3") int size) {

        Long userId = principalDetail.getId();
        PageRequest pageRequest = PageRequest.of(page, size);
        ReservationSliceResponseDto responseDto = reservationService.findAllTerminateReservationByPage(userId, pageRequest);
        return new BaseResponse<>(responseDto);
    }
}
