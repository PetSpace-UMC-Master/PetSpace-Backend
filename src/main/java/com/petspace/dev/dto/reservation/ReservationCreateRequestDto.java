package com.petspace.dev.dto.reservation;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Room;
import com.petspace.dev.domain.Status;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
public class ReservationCreateRequestDto {

    @NotBlank(message = "인원수를 입력해주세요.")
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
                .status(Status.PENDING) //처음 reservation을 create할 때는 admin이 승인할 때까지 PENDING상태이다.
                .build();
    }
}
