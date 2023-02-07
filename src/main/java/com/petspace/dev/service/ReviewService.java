package com.petspace.dev.service;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Review;
import com.petspace.dev.domain.image.ReviewImage;
import com.petspace.dev.dto.review.ReviewDeleteResponseDto;
import com.petspace.dev.dto.review.ReviewRequestDto;
import com.petspace.dev.dto.review.ReviewResponseDto;
import com.petspace.dev.dto.review.ReviewsResponseDto;
import com.petspace.dev.dto.review.ReviewsSliceResponseDto;
import com.petspace.dev.repository.ReservationRepository;
import com.petspace.dev.repository.ReviewImageRepository;
import com.petspace.dev.repository.ReviewRepository;
import com.petspace.dev.repository.UserRepository;
import com.petspace.dev.util.exception.ReviewException;
import com.petspace.dev.util.exception.UserException;
import com.petspace.dev.util.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
        log.info("Review create ! userId={}, reservationId={}", userId, reservationId);

        Reservation reservation = reservationRepository.findByIdAndUserId(reservationId, userId)
                .orElseThrow(() -> new ReviewException(POST_REVIEW_EMPTY_RESERVATION));

        if (reservation.isReviewCreated()) {
            throw new ReviewException(POST_REVIEW_ALREADY_CREATED);
        }

        if (reviewRequestDto.getScore() == null) {
            throw new UserException(POST_REVIEW_EMPTY_SCORE);
        }

        for (MultipartFile file : reviewRequestDto.getReviewImages()) {
            log.info("beforeImage name = [{}]", file.getOriginalFilename());
        }

        Review review = reviewRequestDto.toEntity(reservation);

        log.info("review=[{}][{}]", review.getScore(), review.getContent());
        List<ReviewImage> reviewImages = uploadReviewImages(reviewRequestDto, review);

        for (ReviewImage reviewImage : reviewImages) {
            log.info("reviewImage={}", reviewImage.getReviewImageUrl());
        }
        reviewRepository.save(review);

        return ReviewResponseDto.of(review);
    }

    @Transactional(readOnly = true)
    public ReviewsSliceResponseDto findAllReviewsByPage(Long roomId, Pageable pageable) {
        Slice<Review> allReviewsSliceBy = reviewRepository.findAllReviewsSliceBy(roomId, pageable);

        List<ReviewsResponseDto> reviews = allReviewsSliceBy.getContent().stream()
                .map((ReviewsResponseDto::of))
                .collect(Collectors.toList());

        return ReviewsSliceResponseDto.builder()
                .reviews(reviews)
                .page(allReviewsSliceBy.getPageable().getPageNumber())
                .isLast(allReviewsSliceBy.isLast())
                .build();
    }

    public ReviewResponseDto updateReview(Long userId, Long reviewId, ReviewRequestDto reviewRequestDto) {

        Review review = reviewRepository.findByIdAndUserId(reviewId, userId)
                .orElseThrow(() -> new ReviewException(INVALID_REVIEW));

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

    // TODO Soft delete 관련 상의 후 로직 변경하기
    public ReviewDeleteResponseDto deleteReview(Long userId, Long reviewId) {

        Review review = reviewRepository.findByIdAndUserId(reviewId, userId)
                .orElseThrow(() -> new ReviewException(INVALID_REVIEW));

        Reservation reservation = review.getReservation();
        reservation.deleteReview();
        return ReviewDeleteResponseDto.builder()
                .id(reviewId)
                .build();
    }

    private List<ReviewImage> uploadReviewImages(ReviewRequestDto reviewRequestDto, Review review) {
        return reviewRequestDto.getReviewImages().stream()
                .map(reviewImage -> awsS3Uploader.upload(reviewImage, "review"))
                .map(url -> createPostImage(review, url))
                .collect(Collectors.toList());
    }

    private ReviewImage createPostImage(Review review, String url) {
        log.info("url = {}", url);
        review.clearReviewImages();
        
        return reviewImageRepository.save(ReviewImage.builder()
                .reviewImageUrl(url)
                .review(review)
                .build());
    }

    private List<ReviewImage> uploadReviewImagesV2(List<MultipartFile> reviewImages, Review review) {
        return reviewImages.stream()
                .map(reviewImage -> awsS3Uploader.upload(reviewImage, "review"))
                .map(url -> createPostImage(review, url))
                .collect(Collectors.toList());
    }
}


