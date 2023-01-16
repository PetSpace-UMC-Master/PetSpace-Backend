package com.petspace.dev.service;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Review;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;
import com.petspace.dev.domain.image.ReviewImage;
import com.petspace.dev.dto.review.ReviewCreateRequestDto;
import com.petspace.dev.repository.ReviewImageRepository;
import com.petspace.dev.repository.ReviewRepository;
import com.petspace.dev.repository.ReservationRepository;
import com.petspace.dev.repository.UserRepository;
import com.petspace.dev.util.BaseResponse;
import com.petspace.dev.util.s3.AwsS3Uploader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.petspace.dev.util.BaseResponseStatus.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;
    private final ReviewImageRepository reviewImageRepository;
    private final AwsS3Uploader awsS3Uploader;

    @Transactional
    public BaseResponse save(Long userId, Long reservationId, ReviewCreateRequestDto reviewRequestDto) {

        Optional<User> user = userRepository.findById(userId);
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);


        /**
         1. 유저 ID와 토큰이 안맞을 경우 : JWT 토큰 확인 - 유저 아이디의 이메일과 JWT 디코딩의 이메일이 같으면 실행 else 에러
         2. 방이 존재하지 않는 경우 => OK
         3. 스코어가 없는 경우
         */

//        if (userRepository.findById(userId).isEmpty()) {
//            throw new UserException();
//        }
//
//        if (reservationRepository.findById(reservationId).isEmpty()) {
//            return new BaseResponse(POST_REVIEW_EMPTY_RESERVATION);
//        }

        // toDo : 예외처리
        Reservation reservation1 = reservation.get();
        String content = reviewRequestDto.getContent();
        log.info("before");
        int score = reviewRequestDto.getScore();
        log.info("after");

        Review review = Review.builder()
                .reservation(reservation1)
                .status(Status.ACTIVE)
                .score(score)
                .content(content)
                .build();

        System.out.println("review_content : " + review.getContent());
        List<ReviewImage> reviewImages = uploadReviewImages(reviewRequestDto, review);

        return new BaseResponse(review.getId());
    }

    private List<ReviewImage> uploadReviewImages(ReviewCreateRequestDto reviewRequestDto, Review review) {
        return reviewRequestDto.getReviewImages().stream()
                .map(reviewImage -> awsS3Uploader.upload(reviewImage, "post"))
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


