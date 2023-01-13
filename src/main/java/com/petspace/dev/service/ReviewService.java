package com.petspace.dev.service;

import com.petspace.dev.domain.Reservation;
import com.petspace.dev.domain.Review;
import com.petspace.dev.domain.Status;
import com.petspace.dev.domain.User;
import com.petspace.dev.dto.review.ReviewCreateRequestDto;
import com.petspace.dev.repository.ReviewRepository;
import com.petspace.dev.repository.ReservationRepository;
import com.petspace.dev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    public Long save(Long userId, Long reservationId, ReviewCreateRequestDto reviewRequestDto) {

        Optional<User> user = userRepository.findById(userId);
        Optional<Reservation> reservation = reservationRepository.findById(reservationId);
        System.out.println("reservation : " + reservation);

        // toDo : 예외처리
        // toDo : 예외처리
        // toDo : JWT 토큰 확인 - 유저 아이디의 이메일과 JWT 디코딩의 이메일이 같으면 실행 else 에러
        // toDo : 이미지 넣어지는지 확인

        User user1 = reservation.get().getUser();
        log.info("user1={}", user1);
        Reservation reservation1 = reservation.get();
        String content = reviewRequestDto.getContent();
        int score = reviewRequestDto.getScore();

        Review review = Review.builder()
                .reservation(reservation1)
                .status(Status.ACTIVE)
                .score(score)
                .content(content)
                .build();

        return reviewRepository.save(review).getId();
    }

}

