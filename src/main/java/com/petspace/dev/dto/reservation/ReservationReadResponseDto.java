package com.petspace.dev.dto.reservation;

import com.petspace.dev.domain.Reservation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Schema(description = "예약 조회 응답 dto")
@Getter
@Builder
@AllArgsConstructor
public class ReservationReadResponseDto {

    @Schema(description = "reservation 코드")
    private String reservationCode;
    @Schema(description = "roomId")
    private Long roomId;
    @Schema(description = "reservation 에 대한 review 생성 여부(true 면 생성, false 면 생성 안됨")
    private boolean reviewCreated;
    @Schema(description = "숙소 이름")
    private String roomName;
    @Schema(description = "숙소 이미지 리스트(최대 5개의 이미지만 가져온다)")
    private List<String> roomImageUrls;
    @Schema(description = "예약 시작 날짜")
    private LocalDate startDate;
    @Schema(description = "예약 종료 날짜")
    private LocalDate endDate;
    @Schema(description = "현재 날짜를 기준으로 예약 시작 날짜가 얼마나 남았는가를 의미함(음수일때 완료된 예약, 0 또는 양수일때 다가오는 예약)")
    private int remainingDays;

    public ReservationReadResponseDto(Reservation reservation) {
        reservationCode = reservation.getReservationCode();
        roomId = reservation.getRoom().getId();
        reviewCreated = reservation.isReviewCreated();
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

