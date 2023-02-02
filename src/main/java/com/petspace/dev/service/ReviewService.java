package com.petspace.dev.service;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Review;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.image.ReviewImage;
import com.petspace.dev.dto.review.ReviewDeleteResponseDto;
import com.petspace.dev.dto.review.ReviewRequestDto;
import com.petspace.dev.dto.review.ReviewResponseDto;
import com.petspace.dev.dto.review.ReviewsResponseDto;
import com.petspace.dev.repository.ReservationRepository;
import com.petspace.dev.repository.ReviewImageRepository;
import com.petspace.dev.repository.ReviewRepository;
import com.petspace.dev.repository.UserRepository;
import com.petspace.dev.util.exception.ReviewException;
import com.petspace.dev.util.exception.UserException;
import com.petspace.dev.util.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.petspace.dev.util.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ReviewService {

    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final ReviewRepository reviewRepository;
    private final AwsS3Uploader awsS3Uploader;

    public ReviewResponseDto save(Long userId, Long reservationId, ReviewRequestDto reviewRequestDto) {

        Reservation reservation = reservationRepository.findByIdAndUserId(reservationId, userId)
                .orElseThrow(() -> new ReviewException(POST_REVIEW_EMPTY_RESERVATION));

        if (reservation.isReviewCreated()) {
            throw new ReviewException(POST_REVIEW_ALREADY_CREATED);
        }

        if (reviewRequestDto.getScore() == null) {
            throw new UserException(POST_REVIEW_EMPTY_SCORE);
        }

        Review review = reviewRequestDto.toEntity(reservation);

        uploadReviewImages(reviewRequestDto, review);
        reviewRepository.save(review);

        return ReviewResponseDto.of(review);
    }

    @Transactional(readOnly = true)
    public List<ReviewsResponseDto> findAllReviews(Long roomId) {
        List<Review> reviews = reviewRepository.findAllRoomId(roomId);
        return reviews.stream()
                .map(ReviewsResponseDto::of)
                .collect(Collectors.toList());
    }

    public ReviewResponseDto updateReview(Long userId, Long reviewId, ReviewRequestDto reviewRequestDto) {

        Review review = reviewRepository.findByIdAndUserId(reviewId, userId)
                .orElseThrow(() -> new ReviewException(UPDATE_REVIEW_INVALID_REVIEW));

        updateEachReviewItem(reviewRequestDto, review);

        return ReviewResponseDto.of(review);
    }

    private void updateEachReviewItem(ReviewRequestDto reviewRequestDto, Review review) {
        if (reviewRequestDto.getScore() != null) {
            review.updateScore(reviewRequestDto.getScore());
        }

        if (reviewRequestDto.getContent() != null) {
            review.updateContent(reviewRequestDto.getContent());
        }

        if (!reviewRequestDto.getReviewImages().isEmpty()) {
            review.updateReviewImages(deleteExistedImagesAndUploadNewImages(reviewRequestDto, review));
        }
    }

    private List<ReviewImage> deleteExistedImagesAndUploadNewImages(ReviewRequestDto reviewRequestDto, Review review) {
        deleteExistedImages(review);
        return uploadReviewImages(reviewRequestDto, review);
    }

    private void deleteExistedImages(Review review) {
        reviewImageRepository.deleteAllByIdInBatch(review.getId());
        deleteS3Images(review);
    }

    private void deleteS3Images(Review review) {
        List<ReviewImage> reviewImages = review.getReviewImages();

        for (ReviewImage reviewImage : reviewImages) {
            String imageKey = reviewImage.getReviewImageUrl().substring(49);
            awsS3Uploader.deleteReviewImage(imageKey);
        }
    }

    public ReviewDeleteResponseDto deleteReview(Long userId, Long reviewId) {
        userRepository.findById(userId).orElseThrow(() -> new ReviewException(POST_REVIEW_EMPTY_USER));
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ReviewException(UPDATE_REVIEW_INVALID_REVIEW));

        review.setStatus(Status.valueOf("INACTIVE"));

        return ReviewDeleteResponseDto.builder()
                .id(review.getId())
                .build();
    }

    private List<ReviewImage> uploadReviewImages(ReviewRequestDto reviewRequestDto, Review review) {
        return reviewRequestDto.getReviewImages().stream()
                .map(reviewImage -> awsS3Uploader.upload(reviewImage, "review"))
                .map(url -> createPostImage(review, url))
                .collect(Collectors.toList());
    }

    private ReviewImage createPostImage(Review review, String url) {
        review.clearReviewImages();
        
        return reviewImageRepository.save(ReviewImage.builder()
                .reviewImageUrl(url)
                .review(review)
                .build());
    }
}


