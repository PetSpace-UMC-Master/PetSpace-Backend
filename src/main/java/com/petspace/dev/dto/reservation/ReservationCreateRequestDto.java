package com.petspace.dev.dto.reservation;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Room;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Schema(description = "예약 생성 요청 dto")
@Getter
public class ReservationCreateRequestDto {

    @NotBlank(message = "인원수를 입력해주세요.")
    @Schema(description = "숙소 이용객 수")
    private int totalGuest;

    @NotBlank(message = "반려동물수를 입력해주세요.")
    @Schema(description = "숙소 동반 반려견 수")
    private int totalPet;

    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")
    @Schema(description = "숙소 이용 시작 날짜", example = "yyyy-mm-dd")
    private String startDate;

    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")
    @Schema(description = "숙소 이용 종료 날짜", example = "yyyy-mm-dd")
    private String endDate;

    //dto -> entity
    public Reservation toEntity(User user, Room room) {
        LocalDate startDate = LocalDate.parse(this.startDate, DateTimeFormatter.ISO_LOCAL_DATE); //String 을 LocalDate 로 변환
        LocalDate endDate = LocalDate.parse(this.endDate, DateTimeFormatter.ISO_LOCAL_DATE); //String 을 LocalDate 로 변환
        return Reservation.builder()
                .user(user)
                .room(room)
                .reservationCode(UUID.randomUUID().toString()) //reservationCode 생성
                .totalGuest(totalGuest)
                .totalPet(totalPet)
                .startDate(startDate.atTime(room.getCheckinTime().toLocalTime()))
                .endDate(endDate.atTime(room.getCheckoutTime().toLocalTime()))
                .totalPrice(Period.between(startDate, endDate).getDays() * room.getPrice())
                .isReviewCreated(false) // 처음 예약이 생성될 때 리뷰 생성 상태는 false
                .status(Status.PENDING) //처음 reservation을 create할 때는 admin이 승인할 때까지 PENDING상태이다.
                .build();
    }
}
