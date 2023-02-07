package com.petspace.dev.dto.review;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Review;
import com.petspace.dev.domain.Status;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Slf4j
@Builder
public class ReviewRequestDto {
    private List<MultipartFile> reviewImages;
    private Integer score;
    private String content;

    public Review toEntity(Reservation reservation) {
        return Review.builder()
                .user(reservation.getUser())
                .reservation(reservation)
                .room(reservation.getRoom())
                .status(Status.ACTIVE)
                .score(score)
                .content(content)
                .build();
    }
}
