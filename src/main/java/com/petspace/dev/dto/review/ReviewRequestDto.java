package com.petspace.dev.dto.review;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Review;
import com.petspace.dev.domain.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;
import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
public class ReviewRequestDto {
    private List<MultipartFile> reviewImages = new ArrayList<>();
    private Integer score;
    private String content;

    public Review toEntity(Reservation reservation) {
        return Review.builder()
                .reservation(reservation)
                .status(Status.ACTIVE)
                .score(score)
                .content(content)
                .build();
    }
}
