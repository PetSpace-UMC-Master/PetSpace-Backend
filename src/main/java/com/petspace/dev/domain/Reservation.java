package com.petspace.dev.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.petspace.dev.domain.user.User;
import com.petspace.dev.util.exception.ReservationException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.petspace.dev.util.BaseResponseStatus.PATCH_RESERVATION_INVALID_RESERVATION_STATUS;
import static com.petspace.dev.util.BaseResponseStatus.POST_RESERVATION_INVALID_ROOM_AVAILABLE_STATUS;

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
            roomAvailable.changeStatus(Status.ACTIVE);
        }
        this.changeStatus(Status.INACTIVE);
    }

    public void addReview(Review review) {
        this.review = review;
        this.isReviewCreated = true;
    }

    public void deleteReview() {
        this.review.changeStatus();
    }

    public void changeStatus(Status status) {
        this.status = status;
    }
}
