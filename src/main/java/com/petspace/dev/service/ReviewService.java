package com.petspace.dev.service;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Review;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.user.User;
import com.petspace.dev.domain.image.ReviewImage;
import com.petspace.dev.dto.review.*;
import com.petspace.dev.repository.ReviewImageRepository;
import com.petspace.dev.repository.ReservationRepository;
import com.petspace.dev.repository.ReviewRepository;
import com.petspace.dev.repository.UserRepository;
import com.petspace.dev.util.BaseResponse;
import com.petspace.dev.util.exception.ReviewException;
import com.petspace.dev.util.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.petspace.dev.util.exception.UserException;

import java.util.*;
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

    public Page<ReviewListResponseDto> findAllReview(Pageable pageable) {
        List<Review> reviewGroup = reviewRepository.findAllDesc(pageable);
        List<ReviewListResponseDto> dtos = new ArrayList<>();

        for (Review review : reviewGroup) {
            List<ReviewImage> reviewImages = review.getReviewImages();

            ReviewListResponseDto responseDto = ReviewListResponseDto.builder()
                    .id(review.getId())
                    .nickName(review.getReservation().getUser().getNickname())
                    .reviewImage(reviewImages)
                    .score(review.getScore())
                    .content(review.getContent())
                    .createdDate(review.getCreatedAt().toString().substring(0, 10))
                    .createdTime(review.getCreatedAt().toString().substring(11, 19))
                    .status(review.getStatus())
                    .build();

            dtos.add(responseDto);
        }

        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    @Transactional
    public ReviewUpdateResponseDto updateReview(Long userId, Long reviewId, ReviewUpdateRequestDto reviewRequestDto) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ReviewException(POST_REVIEW_EMPTY_USER));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(UPDATE_REVIEW_INVALID_REVIEW));

        if(review.getReservation().getUser().getId().equals(user.getId())) {
            new ReviewException(UPDATE_REVIEW_INVALID_USER);
        }

        if(reviewRequestDto.getScore() != null) {
            review.setScore(reviewRequestDto.getScore());
        }

        if(reviewRequestDto.getContent() != null) {
            review.setContent(reviewRequestDto.getContent());
        }

        if(!reviewRequestDto.getReviewImages().isEmpty()) {
            List<ReviewImage> reviewImages = updateReviewImages(reviewRequestDto, reviewId, review);
        }

        return ReviewUpdateResponseDto.builder()
                .id(review.getId())
                .build();
    }

    @Transactional
    public List<ReviewImage> updateReviewImages(ReviewUpdateRequestDto reviewRequestDto, Long reviewId, Review review) {

        deleteImages(reviewId);

        return reviewRequestDto.getReviewImages().stream()
                .map(reviewImage -> awsS3Uploader.upload(reviewImage, "review"))
                .map(url -> createPostImage(review, url))
                .collect(Collectors.toList());
    }

    @Transactional
    public ReviewDeleteResponseDto deleteReview(Long userId, Long reviewId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new ReviewException(POST_REVIEW_EMPTY_USER));
        Review getReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(UPDATE_REVIEW_INVALID_REVIEW));

        getReview.setStatus(Status.valueOf("INACTIVE"));

        return ReviewDeleteResponseDto.builder()
                .id(getReview.getId())
                .build();
    }

    private void deleteImages(Long reviewId) {
        Review getReview = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(UPDATE_REVIEW_INVALID_REVIEW));

        if (getReview.getReviewImages().size() != 0) {
            reviewImageRepository.deleteAllByIdInBatch(reviewId);
        }

        deleteS3Images(getReview);
    }

    private void deleteS3Images(Review getReview) {
        List<ReviewImage> reviewImages = getReview.getReviewImages();

        for (ReviewImage reviewImage : reviewImages) {
            String imageKey = reviewImage.getReviewImageUrl().substring(49);
            awsS3Uploader.deleteReviewImage(imageKey);
        }
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
}


