package com.petspace.dev.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.petspace.dev.dto.reservation.ReservationCreateRequestDto;
import com.petspace.dev.util.exception.ReservationException;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.petspace.dev.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.petspace.dev.util.BaseResponseStatus.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Reservation extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    @JsonManagedReference
    @OneToOne(mappedBy = "reservation", orphanRemoval = true)
    private Review review;

    @Column(nullable = false)
    private String reservationCode;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private int totalGuest;

    @Column(nullable = false)
    private int totalPet;
    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column(nullable = false)
    private boolean isReviewCreated;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private Status status;

    @Builder
    public Reservation(User user, Room room, String reservationCode, int totalPrice, int totalGuest, int totalPet, LocalDateTime startDate, LocalDateTime endDate, boolean isReviewCreated, Status status) {
        this.user = user;
        user.getReservations().add(this); //연관관계 설정
        this.room = room;
        room.getReservation().add(this); //연관관계 설정
        this.reservationCode = reservationCode;
        this.totalPrice = totalPrice;
        this.totalGuest = totalGuest;
        this.totalPet = totalPet;
        this.startDate = startDate;
        this.endDate = endDate;
        this.isReviewCreated = isReviewCreated;
        this.status = status;
    }

    //==생성 메서드==//
    public static Reservation createReservation(User user, Room room, ReservationCreateRequestDto dto) {
        Reservation reservation = dto.toEntity(user, room);
        //startDate부터 endDate까지 Room의 RoomAvailable을 INACTIVE로 변경
        LocalDate startDate = reservation.getStartDate().toLocalDate();
        LocalDate endDate = reservation.getEndDate().toLocalDate();

        List<RoomAvailable> roomAvailables = reservation.getRoom().getRoomAvailables().stream()
                .filter(roomAvailable -> roomAvailable.getAvailableDay().toLocalDate().compareTo(startDate) >= 0)
                .filter(roomAvailable -> roomAvailable.getAvailableDay().toLocalDate().isBefore(endDate))
                .collect(Collectors.toList());

        if(roomAvailables.isEmpty()) {
            throw new ReservationException(POST_RESERVATION_INVALID_ROOM_AVAILABLE_DATE);
        }

        for(RoomAvailable roomAvailable : roomAvailables) {
            if(roomAvailable.getStatus() != Status.ACTIVE) {
                throw new ReservationException(POST_RESERVATION_INVALID_ROOM_AVAILABLE_STATUS);
            }
            roomAvailable.setStatus(Status.INACTIVE);
        }
        return reservation;
    }

    //==삭제 메서드==//
    public void deleteReservation() {
        if(this.getStatus() != Status.ACTIVE) {
            throw new ReservationException(PATCH_RESERVATION_INVALID_RESERVATION_STATUS);
        }

        LocalDate startDate = this.getStartDate().toLocalDate();
        LocalDate endDate = this.getEndDate().toLocalDate();

        List<RoomAvailable> roomAvailables = this.getRoom().getRoomAvailables().stream()
                .filter(roomAvailable -> roomAvailable.getAvailableDay().toLocalDate().compareTo(startDate) >= 0)
                .filter(roomAvailable -> roomAvailable.getAvailableDay().toLocalDate().isBefore(endDate))
                .collect(Collectors.toList());

        for(RoomAvailable roomAvailable : roomAvailables) {
            if(roomAvailable.getStatus() == Status.ACTIVE) {
                throw new ReservationException(POST_RESERVATION_INVALID_ROOM_AVAILABLE_STATUS);
            }
            roomAvailable.setStatus(Status.ACTIVE);
        }
        this.setStatus(Status.INACTIVE);
    }

    public void addReview(Review review) {
        this.review = review;
        this.isReviewCreated = true;
    }

    // TODO 상의 후 수정 필요
    public void deleteReview() {
        this.review.changeStatus();
        this.isReviewCreated = false;
    }
}
