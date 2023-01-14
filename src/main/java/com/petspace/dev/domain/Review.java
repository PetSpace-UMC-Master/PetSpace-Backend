package com.petspace.dev.domain;

import com.petspace.dev.domain.image.ReviewImage;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private Reservation reservation;

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
    public Review(Reservation reservation, List<ReviewImage> reviewImages, int score, String content, Status status) {
        this.reservation = reservation;
        this.reviewImages = reviewImages;
        this.score = score;
        this.content = content;
        this.status = status;
    }
}
