package com.petspace.dev.service;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Review;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.user.User;
import com.petspace.dev.domain.image.ReviewImage;
import com.petspace.dev.dto.review.ReviewCreateRequestDto;
import com.petspace.dev.dto.review.ReviewCreateResponseDto;
import com.petspace.dev.dto.review.ReviewListResponseDto;
import com.petspace.dev.repository.ReviewImageRepository;
import com.petspace.dev.repository.ReservationRepository;
import com.petspace.dev.repository.ReviewRepository;
import com.petspace.dev.repository.UserRepository;
import com.petspace.dev.util.exception.ReviewException;
import com.petspace.dev.util.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.petspace.dev.util.exception.UserException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.petspace.dev.util.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewRepository reviewRepository;
    private final AwsS3Uploader awsS3Uploader;

    @Transactional
    public ReviewCreateResponseDto save(Long userId, Long reservationId, ReviewCreateRequestDto reviewRequestDto) {

        User user = userRepository.findById(userId).orElseThrow(() -> new ReviewException(POST_REVIEW_EMPTY_USER));
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReviewException(POST_REVIEW_EMPTY_RESERVATION));

        /**
         1. 유저 ID와 토큰이 안맞을 경우 : JWT 토큰 확인 - 유저 아이디의 이메일과 JWT 디코딩의 이메일이 같으면 실행 else 에러
         2. 방이 존재하지 않는 경우 => OK
         3. 스코어가 없는 경우 => OK
         */

        if (reviewRequestDto.getScore() == null) {
            throw new UserException(POST_REVIEW_EMPTY_SCORE);
        }

        String content = reviewRequestDto.getContent();

        Review review = Review.builder()
                .reservation(reservation)
                .status(Status.ACTIVE)
                .score(reviewRequestDto.getScore())
                .content(content)
                .build();
        List<ReviewImage> reviewImages = uploadReviewImages(reviewRequestDto, review);


        return ReviewCreateResponseDto.builder()
                .id(review.getId())
                .build();
    }

    private List<ReviewImage> uploadReviewImages(ReviewCreateRequestDto reviewRequestDto, Review review) {
        return reviewRequestDto.getReviewImages().stream()
                .map(reviewImage -> awsS3Uploader.upload(reviewImage, "review"))
                .map(url -> createPostImage(review, url))
                .collect(Collectors.toList());
    }

    private ReviewImage createPostImage(Review review, String url) {
        return reviewImageRepository.save(ReviewImage.builder()
                .reviewImageUrl(url)
                .review(review)
                .build());
    }

    public Page<ReviewListResponseDto> findAll(Pageable pageable) {
        List<Review> reviewGroup = reviewRepository.findAllDesc(pageable);
        List<ReviewListResponseDto> dtoList = new ArrayList<>();

        for(Review review : reviewGroup) {
            List<ReviewImage> reviewImages = review.getReviewImages();

            ReviewListResponseDto responseDto = ReviewListResponseDto.builder()
                    .id(review.getId())
                    .nickName(review.getReservation().getUser().getNickname())
                    .reviewImage(reviewImages)
                    .score(review.getScore())
                    .content(review.getContent())
                    .createdDate(review.getCreatedAt().toString().substring(0,10))
                    .createdTime(review.getCreatedAt().toString().substring(11, 19))
                    .status(review.getStatus())
                    .build();

            dtoList.add(responseDto);
        }

        return new PageImpl<>(dtoList, pageable, dtoList.size());
    }
}


