package com.petspace.dev.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.petspace.dev.dto.reservation.ReservationCreateRequestDto;
import lombok.*;
import net.minidev.json.annotate.JsonIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.Period;

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
        reservation.getTotalPrice();
        return reservation;
    }

    //==조회 로직==//
    public int getTotalPrice() {
        int totalPrice = 0;
        Period period = Period.between(startDate.toLocalDate(), endDate.toLocalDate());
        totalPrice = room.getPrice() * period.getDays();
        this.totalPrice = totalPrice;
        return totalPrice;
    }
    public void addReview(Review review) {
        this.review = review;
    }
}
