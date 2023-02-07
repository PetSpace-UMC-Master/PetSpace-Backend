package com.petspace.dev.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.petspace.dev.domain.image.ReviewImage;
import com.petspace.dev.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;

import static com.petspace.dev.domain.Status.INACTIVE;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @Column(nullable = false)
    private int score;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @JsonManagedReference
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @Builder
    public Review(User user, Reservation reservation, Room room, int score, String content, Status status) {
        this.user = user;
        if (user != null) {
            user.addReview(this);
        }

        this.reservation = reservation;
        if (reservation != null) {
            reservation.addReview(this);
        }

        this.room = room;
        if (room != null) {
            room.addReview(this);
        }

        this.score = score;
        this.content = content;
        this.status = status;
    }

    public void updateScore(int score) {
        this.score = score;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateReviewImages(List<ReviewImage> reviewImages) {
        this.reviewImages = reviewImages;
    }

    public void changeStatus() {
        this.status = INACTIVE;
    }

    public void clearReviewImages() {
        this.reviewImages.clear();
    }
}
