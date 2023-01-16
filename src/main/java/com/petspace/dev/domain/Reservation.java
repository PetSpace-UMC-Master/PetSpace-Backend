package com.petspace.dev.domain;

import com.petspace.dev.dto.reservation.ReservationCreateRequestDto;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import java.time.LocalDateTime;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToOne(mappedBy = "reservation")
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
        return reservation;
    }
}
