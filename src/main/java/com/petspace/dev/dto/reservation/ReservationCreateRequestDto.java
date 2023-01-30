package com.petspace.dev.dto.reservation;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Room;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.user.User;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

@Getter
public class ReservationCreateRequestDto {

    @NotBlank(message = "인원수를 입력해주세요.")
    private int totalGuest;

    @NotBlank(message = "반려동물수를 입력해주세요.")
    private int totalPet;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate startDate;

    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDate endDate;

    //dto -> entity
    public Reservation toEntity(User user, Room room) {
        return Reservation.builder()
                .user(user)
                .room(room)
                .reservationCode(UUID.randomUUID().toString()) //reservationCode 생성
                .totalGuest(totalGuest)
                .totalPet(totalPet)
                .startDate(startDate.atTime(room.getCheckinTime().toLocalTime()))
                .endDate(endDate.atTime(room.getCheckoutTime().toLocalTime()))
                .totalPrice(Period.between(startDate, endDate).getDays() * room.getPrice())
                .status(Status.PENDING) //처음 reservation을 create할 때는 admin이 승인할 때까지 PENDING상태이다.
                .build();
    }
}
