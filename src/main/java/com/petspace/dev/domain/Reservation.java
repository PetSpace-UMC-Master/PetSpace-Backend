package com.petspace.dev.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.petspace.dev.dto.reservation.ReservationCreateRequestDto;
import com.petspace.dev.util.BaseResponseStatus;
import com.petspace.dev.util.exception.ReservationException;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
import com.petspace.dev.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.List;

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

    @OneToOne(mappedBy = "reservation", orphanRemoval = true)
    private Review review;

    @Column(nullable = false)
    private String reservationCode;

    @Column(nullable = false)
    private int totalPrice;

    @Column(nullable = false)
    private int totalGuest;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private Status status;

    //==연관관계 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getReservations().add(this);
    }
    public void setRoom(Room room) {
        this.room = room;
        room.getReservation().add(this);
    }
    @Builder
    public Reservation(User user, Room room, String reservationCode, int totalPrice, int totalGuest, LocalDateTime startDate, LocalDateTime endDate, Status status) {
        this.user = user;
        this.room = room;
        this.reservationCode = reservationCode;
        this.totalPrice = totalPrice;
        this.totalGuest = totalGuest;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    //==생성 메서드==//
    public static Reservation createReservation(User user, Room room, ReservationCreateRequestDto dto) {
        Reservation reservation = dto.toEntity();
        reservation.setUser(user);
        reservation.setRoom(room);
        reservation.setTotalPrice();
        //startDate부터 endDate까지 Room의 RoomAvailable을 INACTIVE로 변경
        for(RoomAvailable roomAvailable : reservation.getRoom().getRoomAvailables()) {
            if(roomAvailable.getAvailableDay().toLocalDate().compareTo(reservation.getStartDate().toLocalDate()) >= 0
            && roomAvailable.getAvailableDay().toLocalDate().isBefore(reservation.getEndDate().toLocalDate())) {
                if(roomAvailable.getStatus() != Status.ACTIVE){
                    throw new ReservationException(POST_RESERVATION_INVALID_ROOM_STATUS);
                }
                roomAvailable.setStatus(Status.INACTIVE);
            }
        }
        return reservation;
    }

    //==삭제 메서드==//
    public static void deleteReservation(Reservation reservation) {
        if(reservation.getStatus() != Status.ACTIVE) {
            throw new ReservationException(PATCH_RESERVATION_INVALID_RESERVATION_STATUS);
        }
        for(RoomAvailable roomAvailable : reservation.getRoom().getRoomAvailables()) {
            roomAvailable.setStatus(Status.ACTIVE);
        }
        reservation.setStatus(Status.INACTIVE);
    }

    //==totalPrice setting 로직==//
    public void setTotalPrice() {
        int totalPrice = 0;
        Period period = Period.between(startDate.toLocalDate(), endDate.toLocalDate());
        totalPrice = room.getPrice() * period.getDays();
        this.totalPrice = totalPrice;
    }
    public void addReview(Review review) {
        this.review = review;
    }
}
