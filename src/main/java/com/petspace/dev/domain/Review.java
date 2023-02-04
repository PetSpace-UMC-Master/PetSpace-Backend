package com.petspace.dev.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.petspace.dev.domain.image.ReviewImage;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.petspace.dev.domain.Status.*;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @JsonBackReference
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

    @JsonManagedReference
    @OneToMany(mappedBy = "review", cascade = CascadeType.ALL)
    private List<ReviewImage> reviewImages = new ArrayList<>();

    @Column(nullable = false)
    private int score;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(length = 45, nullable = false)
    private Status status;

    @Builder
    public Review(Reservation reservation, int score, String content, Status status) {
        this.reservation = reservation;
        if (reservation != null) {
            reservation.addReview(this);
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
